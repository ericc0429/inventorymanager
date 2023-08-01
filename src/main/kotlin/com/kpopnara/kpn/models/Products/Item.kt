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

data class ItemDTO(
    val id: UUID?,
    val type: ProductType,
    val name: String,
    val description: String,
    val gtin: String,
    val price: Double,
    val stock: Iterable<String>,
)

fun Item.toDTO() =
    ProductDTO(
        id = id,
        type = type,
        name = name,
        description = description,
        gtin = gtin,
        price = price,
        stock = stock.map { it.toString() },
    )

fun Item.toView() =
    ItemDTO(
        id = id,
        type = type,
        name = name,
        description = description,
        gtin = gtin,
        price = price,
        stock = stock.map { it.toString() },
    )

data class NewItem(val name: String)

/* @RestController
@RequestMapping("/items")
class ItemController(val service: ItemService) {
  @GetMapping fun items(): Iterable<ItemDTO> = service.findAll()

  @PostMapping fun addItem(@RequestBody newItem: NewItem) = service.save(newItem)
}

@Service
class ItemService(val db: ItemRepo) {
  fun findAll(): Iterable<ItemDTO> = db.findAll().map { it.toView() }

  fun save(newItem: NewItem): ItemDTO =
      db.save(
              Item(
                  id = null,
                  name = newItem.name,
                  gtin = "unknown",
                  price = 0.0,
                  stock = emptySet(),
                  description = "Test Item"
              )
          )
          .toView()
}
 */