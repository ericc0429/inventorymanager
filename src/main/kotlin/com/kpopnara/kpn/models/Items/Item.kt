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
    // Inherited from IAsset
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID?, // Unique identifier
    @Column override var name: String,
    @Column override var gtin: String,
    @Column override var price: Double,
    @Column @OneToMany(mappedBy = "item") override var stock: Set<Stock>,

    // Item Specific
    @Column var description: String,
) : Product(id, name, gtin, price, stock) {}

data class ItemDTO(
    val id: UUID?,
    var name: String,
    var gtin: String,
    var price: Double,
    var stock: Set<Stock>,
    var description: String,
)

fun Item.toView() = ItemDTO(id, name, gtin, price, stock, description)

data class NewItem(var name: String)

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