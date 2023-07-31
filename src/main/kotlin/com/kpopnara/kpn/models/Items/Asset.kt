package com.kpopnara.kpn.models.products

// import org.springframework.data.relational.core.mapping.Table
import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.stock.*
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Product -- Asset
This represents assets that are associated with an artist, but are NOT albums. (think lightsticks, posters, etc.)
*/
@Entity
@DiscriminatorValue("Asset")
class Asset(
    // Inherited from Product
    id: UUID?, // Unique identifier
    name: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,
    @ManyToMany(mappedBy = "assets", targetEntity = Artist::class)
    var artist: Set<Artist>, // Associated artist's UUID
    version: String,
    extras: Set<Product>,
    released: String,

    // Asset-Specific Fields
    var brand: String,
) : ArtistProduct(id, name, gtin, price, stock, version, extras, released) {}

// DTO
data class AssetDTO(
    val id: UUID?,
    var name: String,
    var gtin: String,
    var price: Double,
    var stock: Iterable<String>,
    var artist: Iterable<String>,
    var version: String,
    var extras: Iterable<String>,
    var released: String,
    var brand: String
)

fun Asset.toView() =
    AssetDTO(
        id,
        name,
        gtin,
        price,
        stock.map { it.location.toString() },
        artist.map { it.name },
        version,
        extras.map { it.name },
        released,
        brand
    )

data class NewAsset(var name: String)


/* @RestController
@RequestMapping("/assets")
class AssetController(val service: AssetService) {
  @GetMapping fun assets(): Iterable<AssetDTO> = service.findAll()

  @PostMapping fun addAsset(@RequestBody newAsset: NewAsset) = service.save(newAsset)
}

@Service
class AssetService(val db: AssetRepo) {
  fun findAll(): Iterable<AssetDTO> = db.findAll().map { it.toView() }

  fun save(newAsset: NewAsset): AssetDTO =
      db.save(
              Asset(
                  id = null,
                  name = newAsset.name,
                  gtin = "unknown",
                  price = 0.0,
                  stock = emptySet(),
                  artist = emptySet(),
                  version = "unknown",
                  extras = emptySet(),
                  released = "unknown",
                  brand = "unknown"
              )
          )
          .toView()
} */