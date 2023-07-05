package com.kpopnara.kpn

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

enum class GenderType {
  MALE,
  FEMALE,
  NONBINARY,
  NONE
}

enum class GroupGenderType {
  GIRL,
  BOY,
  COED,
  NONE
}

enum class GroupType {
  GROUPP,
  SUBUNIT,
  SOLO,
  NONE
}

@Entity
class Artist(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: UUID, // Unique identifier
    @Column var name: String,
    @Column var debut: String,
    @Column var group_type: GroupType,
    // Artist-Only:
    @Column var birthday: String?,
    @Column var gender: GenderType?,
    @ManyToMany var group: Set<Artist?>?,

    // Group-Only:
    @Column var group_gender: GroupGenderType?,
    @ManyToMany
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Artist?>?, // List of UUID of members
    // Albums
    @ManyToMany
    @JoinTable(
        name = "artist_album_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    var albums: Set<Album>,
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "artist_asset_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    var assets: Set<Asset>,
)

@Repository interface ArtistRepo : JpaRepository<Artist, UUID>

@RestController
@RequestMapping("/api")
class ArtistController(val service: ArtistService) {
  @GetMapping("/artists") fun artists(): List<Artist> = service.findArtists()

  @GetMapping("/artist/{id}")
  fun getArtist(@PathVariable id: UUID): List<Artist> = service.findArtistById(id)

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
  fun deleteArtist(@PathVariable id: UUID) {
    service.deleteArtistById(id)
  }
}

@Service
class ArtistService(val db: ArtistRepo) {
  fun findArtists(): List<Artist> = db.findAll().toList()

  fun findArtistById(id: UUID): List<Artist> = db.findById(id).toList()

  fun save(artist: Artist) {
    db.save(artist)
  }

  fun deleteArtists() {
    db.deleteAll()
  }

  fun deleteArtistById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
