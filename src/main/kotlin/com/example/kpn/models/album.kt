package com.example.kpn

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Optional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Entity
data class Album(

        val artist: String, // Associated artist's UUID
        val type: String,
        val name: String,
        val version: String?,
        val variation: String?,
        val color: String?, // Maybe make this enumerated too
        val extras: String?,
        val released: String,
        val price: Double,
        val barcode: Int,

        @Id @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1 // Unique identifier
    ) {

  constructor() : this("","","","","","","","", 1.0, 1)
}

@Repository
interface AlbumRepo : CrudRepository<Album, String>

@RestController
class AlbumController(val service: AlbumService) {
  @GetMapping("/albums") fun index(): List<Album> = service.findAlbums()

  @GetMapping("/albums/{id}")
  fun index(@PathVariable id: String): List<Album> = service.findAlbumById(id)

  @PostMapping("/albums")
  fun post(@RequestBody album: Album) {
    service.save(album)
  }

  @PutMapping("albums/{id}")
  fun update(@RequestBody album: Album) {
    service.save(album)
  }

  @DeleteMapping("/albums")
  fun delete() {
    service.deleteAlbums()
  }

  @DeleteMapping("/albums/{id}")
  fun delete(@PathVariable id: String) {
    service.deleteAlbumById(id)
  }
}

@Service
class AlbumService(val db: AlbumRepo) {
  fun findAlbums(): List<Album> = db.findAll().toList()

  fun findAlbumById(id: String): List<Album> = db.findById(id).toList()

  fun save(album: Album) {
    db.save(album)
  }

  fun deleteAlbums() {
    db.deleteAll()
  }

  fun deleteAlbumById(id: String) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
