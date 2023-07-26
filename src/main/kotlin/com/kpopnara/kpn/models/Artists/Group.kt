package com.kpopnara.kpn.models

import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import kotlin.collections.plus
import org.mapstruct.Mapper
import org.springframework.web.bind.annotation.*

enum class AttribType {
  MEMBER,
  ALBUM,
  ASSET
}

/* ENTITY -- Artist -- Group
This represents groups that also contain members of type Person.
*/
@Entity
@DiscriminatorValue("Group")
data class Group(
    // Inherited
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    override var id: UUID?, // Unique identifier
    @Column override var name: String,
    @Column override var debut: String,
    // Albums
    @ManyToMany
    @JoinTable(
        name = "group_album_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    override var albums: Set<Album> = setOf(),
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "group_asset_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    override var assets: Set<Asset> = setOf(),

    // Group Specific Fields
    @Column var type: GroupType,
    @Column var group_gender: GroupGenderType,
    // Table linking group to its members
    @ManyToMany(targetEntity = Artist::class)
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Artist> = setOf(), // List of UUID of members/subunits
) : Artist(id, name, debut, albums, assets) {}

data class GroupDTO(
    val id: UUID?,
    var name: String,
    var debut: String,
    var albums: Iterable<String?>,
    var assets: Iterable<String?>,
    var type: GroupType?,
    var group_gender: GroupGenderType,
    var members: Iterable<String?>
)

@Mapper
interface GroupMapper {
  fun toDTO(group: Group): GroupDTO
  fun toBean(groupDTO: GroupDTO): Group
}

fun Group.toView() =
    GroupDTO(
        if (id != null) id else null,
        if (name != null) name else "",
        if (debut != null) debut else "",
        if (albums != null && albums.size != 0) albums.map { it?.name } else emptySet(),
        if (albums != null && assets.size != 0) assets.map { it?.name } else emptySet(),
        type,
        group_gender,
        if (members.size != 0) members.map { it?.name } else emptySet()
    )

data class NewGroup(var name: String)
