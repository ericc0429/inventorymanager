package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.stock.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.repos.ProductRepo
import com.kpopnara.kpn.repos.StockRepo
import java.util.Optional
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class StockServiceImpl(val stockRepo: StockRepo<Stock>, val productRepo: ProductRepo<Product>) : StockService {

    override fun getStock(id : UUID) : StockDTO {
        val stock = stockRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        return stock.toDTO()
    }

    override fun getStocks() : Iterable<StockDTO> {
        return stockRepo.findAll().map() { it.toDTO() }
    }

    override fun getStocksAtLocation(location: String) : Iterable<StockDTO> {
        for ( loc in LocationType.values() ) {
            if (loc.label == location.uppercase()) return stockRepo.findAllByLocation(loc).map() { it.toDTO() }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun addStock(newStock: NewStock) : StockDTO {
        if (newStock.productId != null) {
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
                    restock_threshold = newStock.restock_threshold,
                    oos_date = newStock.oos_date,
                    ordered = newStock.ordered,
                    order_date = newStock.order_date,
                    tracking = newStock.tracking,
                    catalogId = newStock.catalogId
                )
            )
                .toDTO()
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

    }

    override fun updateStock(id: UUID, editStock: EditStock) : StockDTO {
        val stock = stockRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        stock.exclusive = if (editStock.exclusive != null) editStock.exclusive else stock.exclusive
        stock.count = if (editStock.count != null) editStock.count else stock.count
        stock.restock_threshold = if (editStock.restock_threshold != null) editStock.restock_threshold else stock.restock_threshold
        stock.oos_date = if (editStock.oos_date != null) editStock.oos_date else stock.oos_date
        stock.ordered = if (editStock.ordered != null) editStock.ordered else stock.ordered
        stock.order_date = if (editStock.order_date != null) editStock.order_date else stock.order_date
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