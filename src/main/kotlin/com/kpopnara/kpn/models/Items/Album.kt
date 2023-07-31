package com.kpopnara.kpn.models.products

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.stock.*
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Product -- Album
This represents albums associated with either a group or a solo artist.
*/
@Entity
@DiscriminatorValue("Album")
class Album(
    // Inherited from ArtistProduct
    id: UUID?, // Unique identifier
    name: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,
    @ManyToMany(mappedBy = "albums", targetEntity = Artist::class)
    var artist: Set<Artist>, // Associated artist's UUID
    version: String,
    extras: Set<Product>,
    released: String,

    // Album-Specific Fields
    var discography: String,
    var format: String,
    var color: String,
) : ArtistProduct(id, name, gtin, price, stock, version, extras, released) {}

// Data Object
data class AlbumDTO(
    val id: UUID?,
    var name: String,
    var gtin: String,
    var price: Double,
    var stock: Iterable<String>,
    var artist: Iterable<String>,
    var version: String,
    var extras: Iterable<String>,
    var released: String,
    var discography: String,
    var format: String,
    var color: String,
)

// Entity to Data Object Conversion Function
fun Album.toView() =
    AlbumDTO(
        id,
        name,
        gtin,
        price,
        stock.map { it.location.toString() },
        artist.map { it.name },
        version,
        extras.map { it.name },
        released,
        discography,
        format,
        color
    )

// Class for holding POST data
data class NewAlbum(var name: String)

/* @RestController
@RequestMapping("/albums")
class AlbumController(val service: AlbumService) {
  @GetMapping fun albums(): Iterable<AlbumDTO> = service.findAll()

  @PostMapping fun addAlbum(@RequestBody newAlbum: NewAlbum) = service.save(newAlbum)
}

@Service
class AlbumService(val db: AlbumRepo) {
  fun findAll(): Iterable<AlbumDTO> = db.findAll().map { it.toView() }

  fun save(newAlbum: NewAlbum): AlbumDTO =
      db.save(
              Album(
                  id = null,
                  name = newAlbum.name,
                  gtin = "unknown",
                  price = 0.0,
                  stock = emptySet(),
                  artist = emptySet(),
                  version = "unknown",
                  extras = emptySet(),
                  released = "unknown",
                  discography = "unknown",
                  format = "unknown",
                  color = "unknown",
              )
          )
          .toView()
}
 */
