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

/* ENTITY -- Product -- Asset
This represents assets that are associated with an artist, but are NOT albums. (think lightsticks, posters, etc.)
*/
@Entity
@DiscriminatorValue("Asset")
class Asset(
    // Inherited from Product
    id: UUID?, // Unique identifier
    type: ProductType,
    name: String,
    description: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,

    // Inherited from IAsset
    @ManyToMany(mappedBy = "assets", targetEntity = Artist::class)
    override var artist: Set<Artist>, // Associated artist's UUID
    override var version: String,
    @ManyToMany(targetEntity = Product::class)
    @JoinTable(
        name = "asset_extras_jt",
        joinColumns = [JoinColumn(name = "asset_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    override var extras: Set<Product>,
    override var released: String,

    // Asset-Specific Fields
    var brand: String,
    
) : Product(id, type, name, description, gtin, price, stock), IAsset {}

// DTO
data class AssetDTO(
    val id: UUID?,
    val type: ProductType,
    val name: String,
    val description: String,
    val gtin: String,
    val price: Double,
    val stock: Iterable<StockDTO>,
    val artist: Iterable<String>,
    val version: String,
    val extras: Iterable<String>,
    val released: String,
    val brand: String
)

fun Asset.toDTO() =
    AssetDTO(
        id = id,
        type = type,
        name = name,
        description = description,
        gtin = gtin,
        price = price,
        stock = stock.map { it.toDTO() },
        artist = artist.map { it.name },
        version = version,
        extras = extras.map { it.name },
        released = released,
        brand = brand
    )

fun Asset.toProductDTO() =
    ProductDTO(
        id = id,
        type = type,
        name = name,
        description = description,
        gtin = gtin,
        price = price,
        stock = stock.map { it.toDTO() },
    )

data class NewAsset(var name: String)
