package com.kpopnara.kpn.models

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Item -- Asset
This represents assets that are associated with an artist, but are NOT albums. (think lightsticks, posters, etc.)
*/
@Entity
@Table(name = "assets")
class Asset(
    // Inherited from Item
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID?, // Unique identifier
    @Column override var name: String,
    @Column override var gtin: String,
    @Column override var price: Double,
    @Column @OneToMany(mappedBy = "item") override var stock: Set<Stock>,

    // Inherited from IAsset Interface
    @Column
    @ManyToMany(targetEntity = Artist::class)
    override var artist: Set<Artist>, // Associated artist's UUID
    @Column override var version: String,
    // Join Table mapping extras that come with product
    @Column
    @ManyToMany(targetEntity = Item::class)
    @JoinTable(
        name = "asset_extras_jt",
        joinColumns = [JoinColumn(name = "asset_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    override var extras: Set<Item>,
    // @Column override var extras: String,
    @Column override var released: String,

    // Asset-Specific Fields
    // @Column var group: Group,
    @Column var brand: String,
) : Item(id, name, gtin, price, stock), IAsset {}

// DTO
data class AssetDTO(
    val id: UUID?,
    var name: String,
    var gtin: String,
    var price: Double,
    var stock: Set<Stock>,
    var artist: Set<Artist>,
    var version: String,
    var extras: Set<Item>,
    var released: String,
    var brand: String
)

fun Asset.toView() =
    AssetDTO(id, name, gtin, price, stock, artist, version, extras, released, brand)

data class NewAsset(var name: String)

// Repository
@Repository interface AssetRepo : JpaRepository<Asset, UUID>

@RestController
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
}

/*
@RestController
@RequestMapping("/api")
class AssetController(val service: AssetService) {
  @GetMapping("/assets") fun assets(): List<Asset> = service.findAssets()

  @GetMapping("/asset/{id}")
  fun getAsset(@PathVariable id: UUID): List<Asset> = service.findAssetById(id)

  @PostMapping("/assets")
  fun postAsset(@RequestBody asset: Asset) {
    service.save(asset)
  }

  @PutMapping("asset/{id}")
  fun updateAsset(@RequestBody asset: Asset) {
    service.save(asset)
  }

  @DeleteMapping("/assets")
  fun deleteAssets() {
    service.deleteAssets()
  }

  @DeleteMapping("/asset/{id}")
  fun deleteAsset(@PathVariable id: UUID) {
    service.deleteAssetById(id)
  }
}

@Service
class AssetService(val db: AssetRepo) {
  fun findAssets(): List<Asset> = db.findAll().toList()

  fun findAssetById(id: UUID): List<Asset> = db.findById(id).toList()

  fun save(asset: Asset) {
    db.save(asset)
  }

  fun deleteAssets() {
    db.deleteAll()
  }

  fun deleteAssetById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
 */
