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
abstract class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    open val id: UUID?, // Unique identifier
    @Column open var name: String,
    @Column open var gtin: String,
    @Column open var price: Double,
    @Column @OneToMany(mappedBy = "product") open var stock: Set<Stock>,
)

interface IAsset {
  var artist: Set<Artist>
  var version: String
  var extras: Set<Product>
  var released: String
}
