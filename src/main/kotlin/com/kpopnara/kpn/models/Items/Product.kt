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
    open var name: String,
    open var gtin: String,
    open var price: Double,
    @OneToMany(mappedBy = "product") open var stock: Set<Stock>,
)

@Entity
@Table(name = "artistproducts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
open class ArtistProduct (
    id: UUID?,
    name: String,
    gtin: String,
    price: Double,
    stock: Set<Stock>,

    
    // open var artist: Set<Artist>,
    open var version: String,
    @ManyToMany(targetEntity = Product::class)
    @JoinTable(
        name = "ap_extras_jt",
        joinColumns = [JoinColumn(name = "ap_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    open var extras: Set<Product>,
    open var released: String,
) : Product(id, name, gtin, price, stock) {}

/* interface IAsset {
  var artist: Set<Artist>
  var version: String
  var extras: Set<Product>
  var released: String
} */
