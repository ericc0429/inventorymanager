package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Entity
data class Album(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID, // Unique identifier
    @OneToMany val artist: Set<UUID>, // Associated artist's UUID
    val discography: String?,
    val name: String,
    val format: String?,
    val version: String?,
    val color: String?,
    var extras: String?,
    val released: String,
    var price: Double,
    @OneToMany var stock: Set<UUID>?,
) {

  constructor() : this(emptySet(), "", "", "", "", "", "", "", 0.0, emptySet())
}

@Repository interface AlbumRepo : JpaRepository<Album, String>

@RestController
@RequestMapping("/api")
class AlbumController(val service: AlbumService) {
  @GetMapping("/albums") fun albums(): List<Album> = service.findAlbums()

  @GetMapping("/album/{id}")
  fun getAlbum(@PathVariable id: String): List<Album> = service.findAlbumById(id)

  @PostMapping("/albums")
  fun postAlbum(@RequestBody album: Album) {
    service.save(album)
  }

  @PutMapping("album/{id}")
  fun updateAlbum(@RequestBody album: Album) {
    service.save(album)
  }

  @DeleteMapping("/albums")
  fun deleteAlbums() {
    service.deleteAlbums()
  }

  @DeleteMapping("/album/{id}")
  fun deleteAlbum(@PathVariable id: String) {
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
