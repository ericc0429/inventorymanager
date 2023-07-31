package com.kpopnara.kpn.models.artists

import com.kpopnara.kpn.models.products.Album
import com.kpopnara.kpn.models.products.Asset
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.plus
import org.springframework.web.bind.annotation.*

/* ENTITY -- Artist -- Group
This represents groups that also contain members of type Person.
*/
@Entity
@DiscriminatorValue("Group")
class Group(
    // Inherited
    id: UUID?, // Unique identifier
    name: String,
    type: ArtistType,
    debut: String,
    gender: GenderType,
    // Albums
    albums: Set<Album> = setOf(),
    // Other Assets
    assets: Set<Asset> = setOf(),

    // Group Specific Fields
    // Table linking group to its members
    @ManyToMany(targetEntity = Artist::class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Artist> = setOf(), // List of UUID of members/subunits
) : Artist(id, name, type, gender, debut, albums, assets) {}

/* 
data class GroupDTO(
    val id: UUID?,
    var name: String?,
    val type: ArtistType?,
    var debut: String?,
    var gender: GenderType?,
    var albums: Iterable<String?>?,
    var assets: Iterable<String?>?,
    var members: Iterable<String?>?
)

fun Group.toDTO() =
    GroupDTO(
        id,
        name,
        type,
        debut,
        gender,
        albums.map { it.name },
        assets.map { it.name },
        type,
        members.map { it.name }
    )
*/

fun Group.toDTO() =
    ArtistDTO(
        id = id,
        name = name,
        type = type,
        gender = gender,
        debut = debut,
        albums = albums.map { it.name },
        assets = assets.map { it.name },
        members = members.map { it.name },
        birthday = null,
        group = emptySet(),
    )

data class Member(val id: UUID?, val name: String?)

/* data class EditGroup(
    val name: String?,
    val debut: String?,
    val gender: GenderType?,
    val type: GroupType?,
) */

