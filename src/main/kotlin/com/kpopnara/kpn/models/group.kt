package com.kpopnara.kpn

import jakarta.persistence.*
import java.util.Optional
import java.util.UUID
import kotlin.collections.Set
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

enum class GroupTypes {
  GROUP,
  SUBUNIT,
  NONE
}

@Entity
// @Table(name = "groups")
data class Group(
    @Id @GeneratedValue val id: UUID, // Unique identifier
    val name: String,
    val type: GroupTypes,
    @OneToMany(mappedBy = "group") var members: Set<Artist>?, // List of UUID of members
    @OneToMany(mappedBy = "group") var asset_ids: Set<Album>?,
) {
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
