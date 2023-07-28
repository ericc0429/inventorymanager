package com.kpopnara.kpn.models.artists

import com.kpopnara.kpn.models.products.Album
import com.kpopnara.kpn.models.products.Asset
import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import kotlin.collections.plus
import org.mapstruct.Mapper
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
    debut: String,
    gender: GenderType,
    // Albums
    albums: Set<Album> = setOf(),
    // Other Assets
    assets: Set<Asset> = setOf(),

    // Group Specific Fields
    var type: GroupType,
    // Table linking group to its members
    @ManyToMany(targetEntity = Artist::class, fetch = FetchType.EAGER)
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Artist> = setOf(), // List of UUID of members/subunits
) : Artist(id, name, debut, gender, albums, assets) {}

data class GroupDTO(
    val id: UUID?,
    var name: String,
    var debut: String,
    var gender: GenderType,
    var albums: Iterable<String?>,
    var assets: Iterable<String?>,
    var type: GroupType?,
    var members: Iterable<String?>
)

@Mapper
interface GroupMapper {
  fun toDTO(group: Group): GroupDTO
  fun toBean(groupDTO: GroupDTO): Group
}

fun Group.toView() =
    GroupDTO(
        id,
        name,
        debut,
        gender,
        albums.map { it.name },
        assets.map { it.name },
        type,
        members.map { it.name }
    )

data class NewGroup(var name: String)
