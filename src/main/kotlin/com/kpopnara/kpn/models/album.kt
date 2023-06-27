package com.kpopnara.kpn

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Entity
@Table(name = "albums")
data class Album(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID, // Unique identifier
    @ManyToMany var artist: Set<Artist>, // Associated artist's UUID
    @ManyToOne var group: Group,
    var discography: String,
    var name: String,
    var format: String,
    var version: String,
    var color: String,
    var extras: String,
    var released: String,
    var price: Double,
    @OneToMany(mappedBy = "asset") var stock: Set<Stock>,
) {

  // constructor() : this(emptySet(), "", "", "", "", "", "", "", 0.0, emptySet())
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
