package com.kpopnara.kpn

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

enum class LocationType {
  SOUTHFIELD_MI,
  KTOWN_NYC,
  SOUTHLOOP_CHI,
  AMERICANDREAM_NJ,
  NONE
}

@Entity(name = "Stock")
// @Table(name = "stock")
class Stock(
    @Column @Enumerated(EnumType.ORDINAL) val location: LocationType,
    @Nullable @ManyToOne @JoinColumn(name = "asset_id") var item: Item?,
    @Column var count: Int,
    @Column var restock_threshold: Int,
    // Last date item went out of stock -- to help prevent accidental double-orders.
    @Column var oos_date: String,
    // We should have a manual clear button on front-end to set this to false.
    @Column var ordered: Boolean,
    // Date last restock shipment was ordered.
    @Column var order_date: String,
    // Tracking number
    @Column var tracking: String?,

    @Column(unique = true)
    var catalogId : String,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID?=null // Unique identifier
) {
//    constructor(location: LocationType,
//                item: Item, count: Int,
//                restock_threshold: Int,
//                oos_date: String,
//                ordered: Boolean,
//                order_date: String,
//                tracking: String,
//                catalogId : String) : this( ) {
//        this.location = location
//        this.item = item
//    }
}

@Repository
interface StockRepo : JpaRepository<Stock, UUID> {
    fun findByCatalogId(catalogId: String) : Stock?
}

@RestController
@RequestMapping("/api")
class StockController(val service: StockService) {
  @GetMapping("/stocks") fun stocks(): List<Stock> = service.findStocks()

  @GetMapping("/stock/{id}")
  fun getStock(@PathVariable id: UUID): List<Stock> = service.findStockById(id)

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
  fun deleteStock(@PathVariable id: UUID) {
    service.deleteStockById(id)
  }
}

@Service
class StockService(val db: StockRepo) {
  fun findStocks(): List<Stock> = db.findAll().toList()

  fun findStockById(id: UUID): List<Stock> = db.findById(id).toList()

  fun save(stock: Stock) {
    db.save(stock)
  }

  fun deleteStocks() {
    db.deleteAll()
  }

  fun deleteStockById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
