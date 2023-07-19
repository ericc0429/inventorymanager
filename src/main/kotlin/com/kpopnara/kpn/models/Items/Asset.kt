package com.kpopnara.kpn

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import org.hibernate.annotations.Type
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import kotlin.reflect.typeOf

/* ENTITY -- Item -- Asset
This represents assets that are associated with an artist, but are NOT albums. (think lightsticks, posters, etc.)
*/
@Entity
@Table(name = "assets")
class Asset(
    // Inherited from Item

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
    @Column(unique = true)
    override var catalogId : String,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID?=null // Unique identifier
) : Item(name, gtin, price, stock, catalogId, id), IAsset {}

@Repository interface AssetRepo : JpaRepository<Asset, UUID> {
    fun findByCatalogId(catalogId: String) : Asset?
}

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
