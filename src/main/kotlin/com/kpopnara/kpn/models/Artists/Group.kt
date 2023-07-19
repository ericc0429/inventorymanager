package com.kpopnara.kpn.models

import jakarta.persistence.*
import java.util.UUID
import kotlin.collections.Set
import kotlin.collections.plus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

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
    override val id: UUID?, // Unique identifier
    @Column override var name: String,
    @Column override var debut: String,
    // Albums
    @ManyToMany
    @JoinTable(
        name = "group_album_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "album_id")]
    )
    override var albums: Set<Album?>,
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "group_asset_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    override var assets: Set<Asset?>,

    // Group Specific Fields
    @Column var type: GroupType,
    @Column var group_gender: GroupGenderType,
    // Table linking group to its members
    @ManyToMany
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Person?>, // List of UUID of members
) : Artist(id, name, debut, albums, assets) {}

data class GroupDTO(
    val id: UUID?,
    var name: String,
    var debut: String,
    var albums: Iterable<String?>,
    var assets: Iterable<String?>,
    var type: GroupType?,
    var group_gender: GroupGenderType?,
    var members: Iterable<String?>
)

fun Group.toView() =
    GroupDTO(
        id,
        name,
        debut,
        albums.map { it?.name },
        assets.map { it?.name },
        type,
        group_gender,
        members.map { it?.name }
    )

data class NewGroup(var name: String)

@Repository interface GroupRepo : JpaRepository<Group, UUID>

data class NewAttrib(val attribId: String)

@RestController
@RequestMapping("/groups")
class GroupController(val service: GroupService) {
  @GetMapping fun findAll(): Iterable<GroupDTO> = service.findAll()

  @PostMapping fun addGroup(@RequestBody newGroup: NewGroup) = service.save(newGroup)

  @PostMapping("{id}/add")
  fun addAttrib(@PathVariable id: UUID, @RequestBody newAttrib: NewAttrib): GroupDTO =
      service.addAttrib(id, newAttrib)
}

@Service
@Transactional
class GroupService(
    val db: GroupRepo,
    val artistRepo: ArtistRepo,
    val albumRepo: AlbumRepo,
    val assetRepo: AssetRepo
) {
  fun findAll(): Iterable<GroupDTO> = db.findAll().map { it.toView() }

  fun save(newGroup: NewGroup): GroupDTO =
      db.save(
              Group(
                  id = null,
                  name = newGroup.name,
                  debut = "unknown",
                  albums = emptySet(),
                  assets = emptySet(),
                  type = GroupType.NONE,
                  group_gender = GroupGenderType.NONE,
                  members = emptySet()
              )
          )
          .toView()

  fun addAttrib(id: UUID, newAttrib: NewAttrib): GroupDTO {
    val group = db.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    val member =
        artistRepo.findById(UUID.fromString(newAttrib.attribId)).orElseThrow {
          ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    val namae = newAttrib.attribId
    println("==== name: $namae")
    return db.save(group.copy(members = group.members.plus(member))).toView()
  }
  /*   fun addAttrib(id: UUID, newAttrib: NewAttrib): GroupDTO {
    val group = db.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
    if (newAttrib.type == AttribType.MEMBER) {
      val member =
          artistRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(members = group.members.plus(member))).toView()
    } else if (newAttrib.type == AttribType.ALBUM) {
      val album =
          albumRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(albums = group.albums.plus(album))).toView()
    } else if (newAttrib.type == AttribType.ASSET) {
      val asset =
          assetRepo.findById(newAttrib.attribId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
          }
      return db.save(group.copy(assets = group.assets.plus(asset))).toView()
    } else return group.toView()
  } */

  // fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}

  /*
  @GetMapping("/group/{id}")
  fun getGroup(@PathVariable id: UUID): List<Group> = service.findGroupById(id)

  @PostMapping("/groups")
  fun postGroup(@RequestBody group: Group) {
    service.save(group)
  }

  @PutMapping("group/{id}")
  fun updateGroup(@RequestBody group: Group) {
    service.save(group)
  }

  @DeleteMapping("/groups")
  fun deleteGroups() {
    service.deleteGroups()
  }

  @DeleteMapping("/group/{id}")
  fun deleteGroup(@PathVariable id: UUID) {
    service.deleteGroupById(id)
  }
  */

  /*   fun findGroupById(id: UUID): List<Group> = db.findById(id).toList()

  fun findGroupByName(name: String): List<Group> = db.findGroupByName(name).toList()

  fun save(group: Group) {
    db.save(group)
  }

  fun deleteGroups() {
    db.deleteAll()
  }

  fun deleteGroupById(id: UUID) {
    db.deleteById(id)
  } */
