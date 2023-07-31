package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.services.ArtistServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/artists")
class ArtistController(val service: ArtistServiceImpl) {
  @GetMapping fun getAllArtists(): Iterable<ArtistDTO> = service.getAll()
  @GetMapping("/groups") fun getGroups(): Iterable<GroupDTO> = service.getGroups()
  @GetMapping("/solo") fun getPeople(): Iterable<PersonDTO> = service.getPeople()

  @PostMapping("/groups") fun addGroup(@RequestBody newGroup: NewGroup) = service.addGroup(newGroup)
  @PostMapping("/solo") fun addPerson(@RequestBody newPerson: NewPerson) = service.addPerson(newPerson)

  @PutMapping("/groups/{id}") fun updateGroup(@PathVariable id: UUID, @RequestBody editGroup: EditGroup) = service.updateGroup(id, editGroup)
  @PutMapping("/solo/{id}") fun updatePerson(@PathVariable id: UUID, @RequestBody editPerson: EditPerson) = service.updatePerson(id, editPerson)

  @PutMapping("/groups/{id}/addMember") fun addPersonToGroup(@PathVariable id: UUID, @RequestBody newMember: NewMember) = service.addPersonToGroup(id, newMember)

  @DeleteMapping("/{id}") fun deleteArtist(@PathVariable id: UUID) = service.deleteArtist(id)
}

/* @RestController
@RequestMapping("/groups")
class GroupController(val service: GroupServiceImpl) {
  @GetMapping fun getGroups(): Iterable<GroupDTO> = service.getGroups()

  @PostMapping fun addGroup(@RequestBody newGroup: NewGroup) = service.addGroup(newGroup)
}

@RestController
@RequestMapping("/person")
class PersonController(val service: PersonServiceImpl) {
  @GetMapping fun getPeople(): Iterable<PersonDTO> = service.getPeople()

  @PostMapping fun addPerson(@RequestBody newPerson: NewPerson) = service.addPerson(newPerson)
}
 */