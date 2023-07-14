package com.kpopnara.kpn.models

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*

/* ENTITY -- Item -- Product
This represents products that aren't associated with a music group (think facemasks, jewelry, etc.)
*/
@Entity(name = "Product")
@Table(name = "product")
class Product(
    // Inherited from IAsset
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID, // Unique identifier
    @Column override var name: String,
    @Column override var gtin: String,
    @Column override var price: Double,
    @Column @OneToMany(mappedBy = "item") override var stock: Set<Stock>,

    // Product Specific
    @Column var description: String,
) : Item(id, name, gtin, price, stock) {

  // constructor() : this(emptySet(), "", "", "", "", "", "", "", 0.0, emptySet())
}

@Repository interface ProductRepo : JpaRepository<Product, UUID>
/*
@RestController
@RequestMapping("/api")
class ProductController(val service: ProductService) {
  @GetMapping("/products") fun products(): List<Product> = service.findProducts()

  @GetMapping("/product/{id}")
  fun getProduct(@PathVariable id: UUID): List<Product> = service.findProductById(id)

  @PostMapping("/products")
  fun postProduct(@RequestBody product: Product) {
    service.save(product)
  }

  @PutMapping("product/{id}")
  fun updateProduct(@RequestBody product: Product) {
    service.save(product)
  }

  @DeleteMapping("/products")
  fun deleteProducts() {
    service.deleteProducts()
  }

  @DeleteMapping("/product/{id}")
  fun deleteProduct(@PathVariable id: UUID) {
    service.deleteProductById(id)
  }
}

@Service
class ProductService(val db: ProductRepo) {
  fun findProducts(): List<Product> = db.findAll().toList()

  fun findProductById(id: UUID): List<Product> = db.findById(id).toList()

  fun save(product: Product) {
    db.save(product)
  }

  fun deleteProducts() {
    db.deleteAll()
  }

  fun deleteProductById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
 */
