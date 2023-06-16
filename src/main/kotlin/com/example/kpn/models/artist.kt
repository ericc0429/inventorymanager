package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

enum class GenderTypes {
  GIRL,
  BOY,
  NONE
}

@Entity
data class Artist(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID, // Unique identifier
    val name: String,
    val birthday: String,
    val gender: GenderTypes,
    // Use a set in case of artist being in a group and its subunits
    @OneToMany /* (mappedBy = "artist") */ val groups: Set<UUID>?,
    val debut: String?,
    @OneToMany /* (mappedBy = "artist") */
    var assets: Set<UUID>?, // List of associated products UUIDs
) {
  constructor() : this("", "", GenderTypes.NONE, emptySet(), "", emptySet())
}

@Repository interface ArtistRepo : JpaRepository<Artist, String>

@RestController
@RequestMapping("/api")
class ArtistController(val service: ArtistService) {
  @GetMapping("/artists") fun artists(): List<Artist> = service.findArtists()

  @GetMapping("/artist/{id}")
  fun getArtist(@PathVariable id: String): List<Artist> = service.findArtistById(id)

  @PostMapping("/artists")
  fun postArtist(@RequestBody artist: Artist) {
    service.save(artist)
  }

  @PutMapping("artist/{id}")
  fun updateArtist(@RequestBody artist: Artist) {
    service.save(artist)
  }

  @DeleteMapping("/artists")
  fun deleteArtists() {
    service.deleteArtists()
  }

  @DeleteMapping("/artist/{id}")
  fun deleteArtist(@PathVariable id: String) {
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
