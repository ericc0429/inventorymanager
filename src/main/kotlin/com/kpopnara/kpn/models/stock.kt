package com.kpopnara.kpn.models.stock

import com.kpopnara.kpn.models.products.*
import jakarta.persistence.*
import java.util.UUID

enum class LocationType(val label: String) {
  MI_SOUTHFIELD("SOUTHFIELD"),
  NYC_KTOWN("KTOWN"),
  CHI_SOUTHLOOP("SOUTHLOOP"),
  NJ_AMERICANDREAM("AMERICANDREAM"),
  NONE("NONE")
}

@Entity
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID?, // Unique identifier
    @Enumerated(EnumType.ORDINAL) val location: LocationType,
    @ManyToOne @JoinColumn(name = "product_id") var product: Product,
    var exclusive: Boolean,
    var count: Int,
    var restock_threshold: Int,
    // Last date item went out of stock -- to help prevent accidental double-orders.
    var oos_date: String,
    // We should have a manual clear button on front-end to set this to false.
    var ordered: Boolean,
    // Date last restock shipment was ordered.
    var order_date: String,
    // Tracking number
    var tracking: String,
    var catalogId : String
) {}

data class StockDTO(
    val id: UUID?,
    val location: LocationType,
    val product: String,
    val exclusive: Boolean,
    val count: Int,
    val restock_threshold: Int,
    val oos_date: String,
    val ordered: Boolean,
    val order_date: String,
    val tracking: String,
    val catalogId: String
)

fun Stock.toDTO() =
    StockDTO(
        id,
        location,
        product.name,
        exclusive,
        count,
        restock_threshold,
        oos_date,
        ordered,
        order_date,
        tracking,
        catalogId
    )

fun Stock.toDTOString(): String {
    return location.toString() + ": " + count.toString()
}

data class NewStock(
    val location: LocationType,
    val productId: UUID?,
    val exclusive: Boolean = false,
    val count: Int = 0,
    val restock_threshold: Int = 0,
    val oos_date: String = "",
    val ordered: Boolean = false,
    val order_date: String = "",
    val tracking: String = "",
    val catalogId: String = ""
)

data class EditStock(
    val exclusive: Boolean?,
    val count: Int?,
    val restock_threshold: Int?,
    val oos_date: String?,
    val ordered: Boolean?,
    val order_date: String?,
    val tracking: String?,
    val catalogId: String?
)