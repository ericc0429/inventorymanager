package com.kpopnara.kpn.models.products

// import org.springframework.data.relational.core.mapping.Table
import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.stock.*
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.web.bind.annotation.*

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
open class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    open val id: UUID?, // Unique identifier
    open var type: ProductType,
    open var name: String,
    open var description: String,
    open var gtin: String,
    open var price: Double,
    @OneToMany(mappedBy = "product") open var stock: Set<Stock>,
)

interface IAsset {
    var artist: Set<Artist>
    var version: String
    var extras: Set<Product>
    var released: String
}

data class ProductDTO(
    val id: UUID?,
    val type: ProductType,
    val name: String,
    val description: String,
    val gtin: String,
    val price: Double,
    val stock: Iterable<String>,
)

data class NewProduct(
    val type: ProductType = ProductType.NONE,
    val name: String,
    val description: String = "",
    val gtin: String = "",
    val price: Double = 0.0,
    val artist: Iterable<String>?,
    val version: String = "",
    val extras: Iterable<String>?,
    val released: String = "",
    val discography: String = "",
    val format: String = "",
    val color: String = "",
    val brand: String = "",
)

data class EditProduct(
    val type: ProductType?,
    val name: String?,
    val description: String?,
    val gtin: String?,
    val price: Double?,
    val artist: Iterable<String>?,
    val version: String?,
    val extras: Iterable<String>?,
    val released: String?,
    val discography: String?,
    val format: String?,
    val color: String?,
    val brand: String?,
)

data class ProductSearchDTO(
    val id: UUID?,
    val name: String?,
)