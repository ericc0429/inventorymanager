package com.kpopnara.kpn.models

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
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
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID?, // Unique identifier
    @Column @Enumerated(EnumType.ORDINAL) val location: LocationType,
    @ManyToOne @JoinColumn(name = "asset_id") var item: Item,
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
) {}

data class StockDTO(
    val id: UUID?,
    val location: LocationType,
    var item: Item,
    var count: Int,
    var restock_threshold: Int,
    var oos_date: String,
    var ordered: Boolean,
    var order_date: String,
    var tracking: String?,
)

fun Stock.toView() =
    StockDTO(id, location, item, count, restock_threshold, oos_date, ordered, order_date, tracking)

data class NewStock(var location: LocationType, var item: Item)

@Repository interface StockRepo : JpaRepository<Stock, UUID>

@RestController
@RequestMapping("/stock")
class StockController(val service: StockService) {
  @GetMapping fun stocks(): Iterable<StockDTO> = service.findAll()

  @PostMapping fun addStock(@RequestBody newStock: NewStock) = service.save(newStock)
}

@Service
class StockService(val db: StockRepo) {
  fun findAll(): Iterable<StockDTO> = db.findAll().map { it.toView() }

  fun save(newStock: NewStock): StockDTO =
      db.save(
              Stock(
                  id = null,
                  location = newStock.location,
                  item = newStock.item,
                  count = 0,
                  restock_threshold = 25,
                  oos_date = "unknown",
                  ordered = false,
                  order_date = "unknown",
                  tracking = "unknown"
              )
          )
          .toView()

  // fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
