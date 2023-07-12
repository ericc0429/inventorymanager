package com.kpopnara.kpn.models

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Item -- Album
This represents albums associated with either a group or a solo artist.
*/
@Entity(name = "Album")
@Table(name = "albums")
class Album(
    // Inherited from IAsset
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID, // Unique identifier
    @Column override var name: String,
    @Column override var gtin: String,
    @Column override var price: Double,
    @Column @OneToMany(mappedBy = "item") override var stock: Set<Stock>,

    // Inherited from IArtistAsset
    @Column
    @ManyToMany(targetEntity = Artist::class)
    override var artist: Set<Artist>, // Associated artist's UUID
    @Column override var version: String,
    // Join Table mapping extras that come with product
    @ManyToMany(targetEntity = Item::class)
    @JoinTable(
        name = "album_extras_jt",
        joinColumns = [JoinColumn(name = "album_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    override var extras: Set<Item>,

    // @Column override var extras: String,
    @Column override var released: String,

    // Album-Specific Fields
    @Column var discography: String,
    @Column var format: String,
    @Column var color: String,
) : Item(id, name, gtin, price, stock), IAsset {}

@Repository interface AlbumRepo : JpaRepository<Album, UUID>

@RestController
@RequestMapping("/api")
class AlbumController(val service: AlbumService) {
  @GetMapping("/albums") fun albums(): List<Album> = service.findAlbums()

  @GetMapping("/album/{id}")
  fun getAlbum(@PathVariable id: UUID): List<Album> = service.findAlbumById(id)

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
  fun deleteAlbum(@PathVariable id: UUID) {
    service.deleteAlbumById(id)
  }
}

@Service
class AlbumService(val db: AlbumRepo) {
  fun findAlbums(): List<Album> = db.findAll().toList()

  fun findAlbumById(id: UUID): List<Album> = db.findById(id).toList()

  fun save(album: Album) {
    db.save(album)
  }

  fun deleteAlbums() {
    db.deleteAll()
  }

  fun deleteAlbumById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
