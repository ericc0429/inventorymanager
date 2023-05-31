package com.example.kpn

import java.util.Optional
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Table("ITEMS")
data class Item(
    @Id var id: String?,
    var gid: String?,
    val category: String,
    val name: String,
    val version: String?,
    val released: String,
    val price: String
)

interface ItemRepo : CrudRepository<Item, String>

@RestController
class ItemController(val service: ItemService) {
  @GetMapping("/items") fun index(): List<Item> = service.findItems()

  @GetMapping("/items/{id}")
  fun index(@PathVariable id: String): List<Item> = service.findItemById(id)

  @PostMapping("/items")
  fun post(@RequestBody item: Item) {
    service.save(item)
  }

  @PutMapping("items/{id}")
  fun update(@RequestBody item: Item) {
    service.save(item)
  }

  @DeleteMapping("/items")
  fun delete() {
    service.deleteItems()
  }

  @DeleteMapping("/items/{id}")
  fun delete(@PathVariable id: String) {
    service.deleteItemById(id)
  }
}

@Service
class ItemService(val db: ItemRepo) {
  fun findItems(): List<Item> = db.findAll().toList()

  fun findItemById(id: String): List<Item> = db.findById(id).toList()

  fun save(item: Item) {
    db.save(item)
  }

  fun deleteItems() {
    db.deleteAll()
  }

  fun deleteItemById(id: String) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
