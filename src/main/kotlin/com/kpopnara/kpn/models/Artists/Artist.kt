package com.kpopnara.kpn.models.artists

import com.kpopnara.kpn.models.products.Album
import com.kpopnara.kpn.models.products.Asset
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import org.springframework.web.bind.annotation.*

@Entity
@Table(name = "artists")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "artist_type", discriminatorType = DiscriminatorType.STRING)
open class Artist(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    open val id: UUID?, // Unique identifier
    open var name: String,
    open var type: ArtistType,
    open var gender: GenderType,
    open var debut: String,
    // Albums
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "artist_album_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    open var albums: Set<Album>,
    // Other Assets
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "artist_asset_jt",
        joinColumns = [JoinColumn(name = "artist_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    open var assets: Set<Asset>,
)

data class ArtistDTO(
    val id: UUID?,
    val name: String,
    val type: ArtistType,
    val gender: GenderType,
    val debut: String,
    val albums: Iterable<String?>,
    val assets: Iterable<String?>,
    // Group
    val members: Iterable<String?>?,
    // Solo Artist
    val birthday: String?,
    val group: Iterable<String?>?
)

data class NewArtist(
    val name: String,
    val type: ArtistType = ArtistType.ARTIST,
    val gender: GenderType?,
    val debut: String?,
    val birthday: String?,
)

data class EditArtist(
    val name: String?,
    val type: ArtistType?,
    val gender: GenderType?,
    val debut: String?,
    val birthday: String?,
)

data class IdNameDTO(val id: UUID?, val name: String?)