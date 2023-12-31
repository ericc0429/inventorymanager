package com.kpopnara.kpn.models.stock

import com.kpopnara.kpn.models.products.*
import jakarta.persistence.*
import java.util.UUID
import java.util.Date
import java.text.SimpleDateFormat

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
    var updated: Date,
    var count_a: Int,
    var updated_a: Date,
    var count_b: Int,
    var updated_b: Date,
    var sales_velocity: Double,
    var sell_through: Double,
    var restock_threshold: Int,
    // Last date item went out of stock -- to help prevent accidental double-orders.
    var oos_date: Date,
    // We should have a manual clear button on front-end to set this to false.
    var ordered: Boolean,
    // Date last restock shipment was ordered.
    var order_date: Date,
    // Tracking number
    var tracking: String,
    var catalogId: String,
) {}

data class StockDTO(
    val id: UUID?,
    val location: LocationType,
    val product_id: UUID?,
    val product_name: String,
    val exclusive: Boolean,
    val count: Int,
    val updated: String,
    val count_a: Int,
    val updated_a: String,
    val count_b: Int,
    val updated_b: String,
    val sales_velocity: Double,
    val sell_through: Double,
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
        product.id,
        product.name,
        exclusive,
        count,
        updated = SimpleDateFormat("MM-dd-yyyy:HH:mm:ss").format(updated).toString(),
        count_a,
        updated_a = SimpleDateFormat("MM-dd-yyyy:HH:mm:ss").format(updated_a).toString(),
        count_b,
        updated_b = SimpleDateFormat("MM-dd-yyyy:HH:mm:ss").format(updated_b).toString(),
        sales_velocity,
        sell_through,
        restock_threshold,
        oos_date = if (oos_date >= Date(1)) SimpleDateFormat("MM-dd-yyyy:HH:mm:ss").format(oos_date).toString() else "",
        ordered,
        order_date = if (order_date >= Date(1)) SimpleDateFormat("MM-dd-yyyy:HH:mm:ss").format(order_date).toString() else "",
        tracking,
        catalogId
    )

fun Stock.toDTOString(): String {
    return location.toString() + ": " + count.toString()
}

data class NewStock(
    val location: LocationType,
    val productId: UUID,
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
    val updated: String?,
    val count_a: Int?,
    val updated_a: String?,
    val count_b: Int?,
    val updated_b: String?,
    val restock_threshold: Int?,
    val oos_date: String?,
    val ordered: Boolean?,
    val order_date: String?,
    val tracking: String?,
    val catalogId: String?
)