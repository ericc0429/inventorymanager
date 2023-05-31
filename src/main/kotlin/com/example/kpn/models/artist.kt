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

@Table("ARTISTS")
data class Artist(
    @Id var id: String?,
    val name: String,
)

interface ArtistRepo : CrudRepository<Artist, String>

@RestController
class ArtistController(val service: ArtistService) {
  @GetMapping("/artists") fun index(): List<Artist> = service.findArtists()

  @GetMapping("/artists/{id}")
  fun index(@PathVariable id: String): List<Artist> = service.findArtistById(id)

  @PostMapping("/artists")
  fun post(@RequestBody artist: Artist) {
    service.save(artist)
  }

  @PutMapping("artists/{id}")
  fun update(@RequestBody artist: Artist) {
    service.save(artist)
  }

  @DeleteMapping("/artists")
  fun delete() {
    service.deleteArtists()
  }

  @DeleteMapping("/artists/{id}")
  fun delete(@PathVariable id: String) {
    service.deleteArtistById(id)
  }
}

@Service
class ArtistService(val db: ArtistRepo) {
  fun findArtists(): List<Artist> = db.findAll().toList()

  fun findArtistById(id: String): List<Artist> = db.findById(id).toList()

  fun save(artist: Artist) {
    db.save(artist)
  }

  fun deleteArtists() {
    db.deleteAll()
  }

  fun deleteArtistById(id: String) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
