package com.example.kpn

import jakarta.persistence.*
import java.util.Optional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

enum class GroupTypes {
  GROUP,
  SUBUNIT,
  NONE
}

@Entity
@Table(name="\"Group\"")
data class Group(
    val name: String,
    val type: GroupTypes,
    @ElementCollection
    var members: Set<String>, // List of UUID of members

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long = -1 // Unique identifier
) {
  constructor() : this("",GroupTypes.NONE, emptySet())
}

@Repository
interface GroupRepo : CrudRepository<Group, String>

@RestController
class GroupController(val service: GroupService) {
  @GetMapping("/groups") fun index(): List<Group> = service.findGroups()

  @GetMapping("/groups/{id}")
  fun index(@PathVariable id: String): List<Group> = service.findGroupById(id)

  @PostMapping("/groups")
  fun post(@RequestBody group: Group) {
    service.save(group)
  }

  @PutMapping("groups/{id}")
  fun update(@RequestBody group: Group) {
    service.save(group)
  }

  @DeleteMapping("/groups")
  fun delete() {
    service.deleteGroups()
  }

  @DeleteMapping("/groups/{id}")
  fun delete(@PathVariable id: String) {
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
