package com.kpopnara.kpn.models

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

/* ENTITY -- Artist -- Group
This represents groups that also contain members of type Person.
*/
@Entity
@DiscriminatorValue("Group")
class Group(
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
    override var albums: Set<Album>,
    // Other Assets
    @ManyToMany
    @JoinTable(
        name = "group_asset_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "asset_id")]
    )
    override var assets: Set<Asset>,

    // Group Specific Fields
    @Column var type: GroupType?,
    @Column var group_gender: GroupGenderType?,
    // Table linking group to its members
    @ManyToMany
    @JoinTable(
        name = "group_person_jt",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    var members: Set<Person>?, // List of UUID of members
) : Artist(id, name, debut, albums, assets) {
  // constructor() : this("", GroupTypes.NONE, emptySet(), emptySet())
}

data class GroupDTO(
    val id: UUID?,
    var name: String,
    var debut: String,
    var albums: Set<Album>,
    var assets: Set<Asset>,
    var type: GroupType?,
    var group_gender: GroupGenderType?,
    var members: Set<Person>?
)

data class NewGroup(val name: String)

fun Group.toView() = GroupDTO(id, name, debut, albums, assets, type, group_gender, members)

@Repository interface GroupRepo : JpaRepository<Group, UUID>

@RestController
@RequestMapping("/groups")
class GroupController(val service: GroupService) {
  @GetMapping fun groups(): Iterable<GroupDTO> = service.findAll()

  @PostMapping fun postGroup(@RequestBody newGroup: NewGroup) = service.save(newGroup)

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
}

@Service
class GroupService(val db: GroupRepo) {
  fun findAll(): Iterable<GroupDTO> = db.findAll().map { it.toView() }

  fun save(newGroup: NewGroup) =
      db.save(
          Group(
              id = null,
              name = newGroup.name,
              debut = "",
              albums = emptySet(),
              assets = emptySet(),
              type = GroupType.NONE,
              group_gender = GroupGenderType.NONE,
              members = emptySet()
          )
      )

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

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
