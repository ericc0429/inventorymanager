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
                count_a = newStock.count,
                updated_a = Date(),
                count_b = newStock.count,
                updated_b = Date(),
                sales_velocity = 0.0,
                sell_through = 0.0,
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
            stock.count = editStock.count
            stock.updated = if (editStock.updated != null) formatter.parse(editStock.updated) else Date()

            // Update count/updated for A & B if previous updated was older than a week
            val updateTimeDiff = TimeUnit.DAYS.convert(Math.abs(stock.updated.getTime() - stock.updated_a.getTime()), TimeUnit.MILLISECONDS)
            if (updateTimeDiff >= 7) {
                stock.count_b = stock.count_a
                stock.updated_b = stock.updated_a

                stock.count_a = stock.count
                stock.updated_a = stock.updated
            }
            
        }
        
        if (editStock.count_a != null) {
            stock.count_a = editStock.count_a
        }
        if (editStock.count_b != null) {
            stock.count_b = editStock.count_b
        }
        if (editStock.updated_a != null && formatter.parse(editStock.updated_a) > stock.updated_b) {
            stock.updated_a = formatter.parse(editStock.updated_a)
        }
        if (editStock.updated_b != null && formatter.parse(editStock.updated_b) < stock.updated_a) {
            stock.updated_b = formatter.parse(editStock.updated_b)
        }

        // Calculating velocity & sell-through
        val timeDiff = TimeUnit.DAYS.convert(Math.abs(stock.updated_a.getTime() - stock.updated_b.getTime()), TimeUnit.MILLISECONDS)
        stock.sales_velocity = (stock.count_b - stock.count_a)/(1.0 * timeDiff)
        stock.sell_through = (stock.count_b - stock.count_a)/(1.0 * stock.count_b)

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