package com.kpopnara.kpn.models.products

import com.kpopnara.kpn.models.stock.*
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Product -- Item
This represents items that aren't associated with a music group (think facemasks, jewelry, etc.)
*/
@Entity
@Table(name = "items")
class Item(
    // Inherited from Product
    id: UUID?, // Unique identifier
    type: ProductType,
    name: String,
    description: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,
) : Product(id, type, name, description, gtin, price, stock) {}

fun Item.toDTO() =
    ProductDTO(
        id = id,
        type = type,
        name = name,
        description = description,
        gtin = gtin,
        price = price,
        stock = stock.map { it.toDTO() },
    )

data class NewItem(val name: String)
