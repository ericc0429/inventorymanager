package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

enum class Location {
  SOUTHFIELD_MI,
  KTOWN_NYC,
  SOUTHLOOP_CHI,
  AMERICANDREAM_NJ,
  NONE
}

@Entity
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID, // Unique identifier
    val location: Location,
    var count: UInt?,
    var restock_threshold: UInt?,
    var ordered: Boolean?,
    var arrival: String?,
) {

  constructor() : this(Location.NONE, 0, 0, false, "")
}

@Repository interface StockRepo : JpaRepository<Stock, String>

@RestController
@RequestMapping("/api")
class StockController(val service: StockService) {
  @GetMapping("/stocks") fun stocks(): List<Stock> = service.findStocks()

  @GetMapping("/stock/{id}")
  fun getStock(@PathVariable id: String): List<Stock> = service.findStockById(id)

  @PostMapping("/stocks")
  fun postStock(@RequestBody stock: Stock) {
    service.save(stock)
  }

  @PutMapping("stock/{id}")
  fun updateStock(@RequestBody stock: Stock) {
    service.save(stock)
  }

  @DeleteMapping("/stocks")
  fun deleteStocks() {
    service.deleteStocks()
  }

  @DeleteMapping("/stock/{id}")
  fun deleteStock(@PathVariable id: String) {
    service.deleteStockById(id)
  }
}

@Service
class StockService(val db: StockRepo) {
  fun findStocks(): List<Stock> = db.findAll().toList()

  fun findStockById(id: String): List<Stock> = db.findById(id).toList()

  fun save(stock: Stock) {
    db.save(stock)
  }

  fun deleteStocks() {
    db.deleteAll()
  }

  fun deleteStockById(id: String) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
