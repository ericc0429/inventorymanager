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
    // Inherited from Product
    id: UUID?, // Unique identifier
    name: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,

    // Inherited from IAsset
    @ManyToMany(mappedBy = "albums", targetEntity = Artist::class)
    override var artist: Set<Artist>, // Associated artist's UUID
    override var version: String,
    @ManyToMany(targetEntity = Product::class)
    @JoinTable(
        name = "album_extras_jt",
        joinColumns = [JoinColumn(name = "album_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    override var extras: Set<Product>,
    override var released: String,

    // Album-Specific Fields
    var discography: String,
    var format: String,
    var color: String,
) : Product(id, ProductType.ALBUM, name, "", gtin, price, stock), IAsset {}

data class AlbumDTO(
    val id: UUID?,
    val type: ProductType,
    val name: String,
    val gtin: String,
    val price: Double,
    val stock: Iterable<StockDTO>,
    val artist: Iterable<String>,
    val version: String,
    val extras: Iterable<String>,
    val released: String,
    val discography: String,
    val format: String,
    val color: String,
)

// Entity to Data Object Conversion Function
fun Album.toDTO() =
    AlbumDTO(
        id,
        ProductType.ALBUM,
        name,
        gtin,
        price,
        stock.map { it.toDTO() },
        artist.map { it.name },
        version,
        extras.map { it.name },
        released,
        discography,
        format,
        color
    )

fun Album.toProductDTO() =
    ProductDTO(
        id = id,
        type = ProductType.ALBUM,
        name = name,
        description = "",
        gtin = gtin,
        price = price,
        stock = stock.map { it.toDTO() },
    )
