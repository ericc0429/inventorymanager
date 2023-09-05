package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.stock.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.ProductRepo
import com.kpopnara.kpn.repos.StockRepo
import java.util.UUID
import java.util.Date
import java.util.concurrent.TimeUnit
import java.text.SimpleDateFormat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.math.roundToInt
import kotlin.math.ceil

@Service
@Transactional
class StockServiceImpl(val stockRepo: StockRepo<Stock>, val productRepo: ProductRepo<Product>) : StockService {

    override fun getStocks() : Iterable<StockDTO> {
        return stockRepo.findAll().map() { it.toDTO() }
    }
    override fun getStockById(id: UUID): StockDTO {
        stockRepo.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        return stockRepo.getReferenceById(id).toDTO()
    }

    override fun getStocksAtLocation(location: String) : Iterable<StockDTO> {
        for ( loc in LocationType.values() ) {
            if (loc.label == location.uppercase()) return stockRepo.findAllByLocation(loc).map() { it.toDTO() }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun addStock(newStock: NewStock) : StockDTO {

        val formatter = SimpleDateFormat("MM-dd-yyyy:HH:mm:ss")
        val product = productRepo.findById(newStock.productId).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND)}
        for (existingStock: Stock in product.stock) {
            if (existingStock.location == newStock.location) throw ResponseStatusException(HttpStatus.CONFLICT)
        }

        return stockRepo.save(
            Stock(
                id = null,
                location = newStock.location,
                product = product,
                exclusive = newStock.exclusive,
                count = newStock.count,
                updated = Date(),
                old_count = newStock.count,
                old_updated = Date(),
                sales_velocity = 0.0,
                // sell_through = 0.0,
                restock_threshold = newStock.restock_threshold,
                oos_date = if (newStock.oos_date != "") formatter.parse(newStock.oos_date) else Date(0),
                ordered = newStock.ordered,
                order_date = if (newStock.order_date != "") formatter.parse(newStock.order_date) else Date(0),
                tracking = newStock.tracking,
                catalogId = newStock.catalogId
            )
        )
        .toDTO()
    }

    override fun updateStock(id: UUID, editStock: EditStock) : StockDTO {
        val stock = stockRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        val formatter = SimpleDateFormat("MM-dd-yyyy:HH:mm:ss")

        stock.exclusive = if (editStock.exclusive != null) editStock.exclusive else stock.exclusive
        /* if (editStock.count != null) {
            if (editStock.count == 0 && stock.count > 0) stock.oos_date = Date()
            stock.count = editStock.count
        } else stock.count */

        if (editStock.count != null) {
            stock.old_count = stock.count
            stock.count = editStock.count
        }
        stock.old_count = if (editStock.old_count != null) editStock.old_count else stock.old_count

        stock.old_updated = if (editStock.old_updated != null) formatter.parse(editStock.old_updated) else stock.updated
        stock.updated = if (editStock.updated != null) formatter.parse(editStock.updated) else Date()

        if (stock.count < stock.old_count) {
            val timeDiff = TimeUnit.DAYS.convert(Math.abs(stock.updated.getTime() - stock.old_updated.getTime()), TimeUnit.MILLISECONDS)
            if (timeDiff > 2) stock.sales_velocity = (stock.old_count - stock.count)/(1.0 * timeDiff)
        }

        stock.restock_threshold = if (editStock.restock_threshold != null) editStock.restock_threshold else (4 * ceil(stock.sales_velocity).roundToInt())
        stock.oos_date = if (editStock.oos_date != null) formatter.parse(editStock.oos_date) else stock.oos_date
        stock.ordered = if (editStock.ordered != null) editStock.ordered else stock.ordered
        stock.order_date = if (editStock.order_date != null) formatter.parse(editStock.order_date) else stock.order_date
        stock.tracking = if (editStock.tracking != null) editStock.tracking else stock.tracking

        return stockRepo.save(stock).toDTO()
    }

    override fun deleteStock(id: UUID) : StockDTO {
        val stock = stockRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (stock.product.stock.size > 0) stock.product.stock = stock.product.stock.minus(stock)
        stockRepo.deleteById(id)
        return stock.toDTO()
        
    }

}