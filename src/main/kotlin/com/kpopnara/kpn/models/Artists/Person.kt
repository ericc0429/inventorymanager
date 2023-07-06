package com.kpopnara.kpn

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Artist -- Person
This represents a solo person or a member of a group.
*/
@Entity
@DiscriminatorValue("Person")
class Person(
    // Inherited
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID, // Unique identifier
    @Column override var name: String,
    @Column override var debut: String,
    // Albums
    @ManyToMany
    @JoinTable(
        name = "person_album_jt",
        joinColumns = [JoinColumn(name = "person_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    override var albums: Set<Album>,
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "person_asset_jt",
        joinColumns = [JoinColumn(name = "person_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    override var assets: Set<Asset>,

    // Artist Specific Fields
    @Column var birthday: String?,
    @Column var gender: GenderType?,
    // Use a set in case of person being in a group and its subunits
    @Column @ManyToMany var group: Set<Group>?,
) : Artist(id, name, debut, albums, assets) {}

@Repository interface ArtistRepo : JpaRepository<Person, UUID>

@RestController
@RequestMapping("/api")
class ArtistController(val service: ArtistService) {
  @GetMapping("/persons") fun persons(): List<Person> = service.findArtists()

  @GetMapping("/person/{id}")
  fun getArtist(@PathVariable id: UUID): List<Person> = service.findArtistById(id)

  @PostMapping("/persons")
  fun postArtist(@RequestBody person: Person) {
    service.save(person)
  }

  @PutMapping("person/{id}")
  fun updateArtist(@RequestBody person: Person) {
    service.save(person)
  }

  @DeleteMapping("/persons")
  fun deleteArtists() {
    service.deleteArtists()
  }

  @DeleteMapping("/person/{id}")
  fun deleteArtist(@PathVariable id: UUID) {
    service.deleteArtistById(id)
  }
}

@Service
class ArtistService(val db: ArtistRepo) {
  fun findArtists(): List<Person> = db.findAll().toList()

  fun findArtistById(id: UUID): List<Person> = db.findById(id).toList()

  fun save(person: Person) {
    db.save(person)
  }

  fun deleteArtists() {
    db.deleteAll()
  }

  fun deleteArtistById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
