package com.kpopnara.kpn.models.stock

import com.kpopnara.kpn.models.products.*
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
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID?, // Unique identifier
    @Column @Enumerated(EnumType.ORDINAL) val location: LocationType,
    @ManyToOne @JoinColumn(name = "product_id") var product: Product,
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
    var product: Product,
    var count: Int,
    var restock_threshold: Int,
    var oos_date: String,
    var ordered: Boolean,
    var order_date: String,
    var tracking: String?,
)

fun Stock.toView() =
    StockDTO(
        id,
        location,
        product,
        count,
        restock_threshold,
        oos_date,
        ordered,
        order_date,
        tracking
    )

fun Stock.toString(): String {
    return location.toString() + ": " + count.toString()
}

data class NewStock(var location: LocationType, var product: Product)

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
                  product = newStock.product,
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
