package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
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
@Table(name = "\"Group\"")
data class Group(
    val name: String,
    val type: GroupTypes,
    @ElementCollection var members: Set<String>, // List of UUID of members
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = -1 // Unique identifier
) {
  constructor() : this("", GroupTypes.NONE, emptySet())
}

@Repository interface GroupRepo : JpaRepository<Group, String>

@RestController
@RequestMapping("/api")
class GroupController(val service: GroupService) {
  @GetMapping("/groups") fun groups(): List<Group> = service.findGroups()

  @GetMapping("/group/{id}")
  fun getGroup(@PathVariable id: String): List<Group> = service.findGroupById(id)

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
  fun deleteGroup(@PathVariable id: String) {
    service.deleteGroupById(id)
  }
}

@Service
class GroupService(val db: GroupRepo) {
  fun findGroups(): List<Group> = db.findAll().toList()

  fun findGroupById(id: String): List<Group> = db.findById(id).toList()

  fun save(group: Group) {
    db.save(group)
  }

  fun deleteGroups() {
    db.deleteAll()
  }

  fun deleteGroupById(id: String) {
    db.deleteById(id)
  }

  fun <T : Any> Optional<out T>.toList(): List<T> = if (isPresent) listOf(get()) else emptyList()
}
