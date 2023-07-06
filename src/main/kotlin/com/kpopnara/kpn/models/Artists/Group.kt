package com.kpopnara.kpn

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
@Table(name = "groups")
@DiscriminatorValue("Group")
class Group(
    // Inherited
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID, // Unique identifier
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

@Repository interface GroupRepo : JpaRepository<Group, UUID>

@RestController
@RequestMapping("/api")
class GroupController(val service: GroupService) {
  @GetMapping("/groups") fun groups(): List<Group> = service.findGroups()

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
}

@Service
class GroupService(val db: GroupRepo) {
  fun findGroups(): List<Group> = db.findAll().toList()

  fun findGroupById(id: UUID): List<Group> = db.findById(id).toList()

  fun save(group: Group) {
    db.save(group)
  }

  fun deleteGroups() {
    db.deleteAll()
  }

  fun deleteGroupById(id: UUID) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
