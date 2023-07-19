package com.kpopnara.kpn

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
import kotlin.collections.Set
import org.springframework.web.bind.annotation.*

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Item(
    @Column open var name: String,
    @Column open var gtin: String,
    @Column open var price: Double,
    @Column @OneToMany(mappedBy = "item") open var stock: Set<Stock>,
    @Column(unique = true)
    open var catalogId : String,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = true, nullable = false)
    open val id: UUID?=null // Unique identifier
)

interface IAsset {
  var artist: Set<Artist>
  var version: String
  var extras: Set<Item>
  var released: String
}
