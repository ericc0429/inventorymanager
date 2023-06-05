package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
import org.springframework.data.relational.core.mapping.Table
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

enum class GenderTypes {
  GIRL,
  BOY,
  NONE
}

@Entity
data class Artist(
        @ElementCollection
        val groups: Set<String>, // Use a set in case of artist being in a group and its subunits
        val name: String,
        val debut: String,
        val label: String,
        val gender: GenderTypes,
        val birthday: String,
        @ElementCollection
        var assets: Set<String>, // List of associated products UUIDs

        @Id @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1 // Unique identifier
) {
  constructor() : this(emptySet(),"","","",GenderTypes.NONE,"",emptySet())

}

@Repository
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
