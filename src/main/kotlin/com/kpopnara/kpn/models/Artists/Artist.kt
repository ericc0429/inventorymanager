package com.kpopnara.kpn.models

// import org.springframework.data.relational.core.mapping.Table
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.web.bind.annotation.*

enum class GenderType {
  MALE,
  FEMALE,
  NONBINARY,
  NONE
}

enum class GroupGenderType {
  GIRL,
  BOY,
  COED,
  NONE
}

enum class GroupType {
  GROUPP,
  SUBUNIT,
  NONE
}

@Entity
@Table(name = "artists")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "artist_type", discriminatorType = DiscriminatorType.STRING)
abstract class Artist(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    open val id: UUID, // Unique identifier
    @Column open var name: String,
    @Column open var debut: String,
    // Albums
    @ManyToMany
    @JoinTable(
        name = "artist_album_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    open var albums: Set<Album>,
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "artist_asset_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    open var assets: Set<Asset>,
)
