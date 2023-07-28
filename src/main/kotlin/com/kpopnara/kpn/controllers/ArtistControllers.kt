package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.services.ArtistServiceImpl
import com.kpopnara.kpn.services.GroupServiceImpl
import com.kpopnara.kpn.services.PersonServiceImpl
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/artists")
class ArtistController(val service: ArtistServiceImpl) {
  @GetMapping fun getAllArtists(): Iterable<Artist> = service.getAll()
  @GetMapping("/groups") fun getGroups(): Iterable<GroupDTO> = service.getGroups()
  @GetMapping("/solo") fun getPeople(): Iterable<PersonDTO> = service.getPeople()

  @PostMapping("/groups") fun addGroup(@RequestBody newGroup: NewGroup) = service.addGroup(newGroup)
  @PostMapping("/solo") fun addPerson(@RequestBody newPerson: NewPerson) = service.addPerson(newPerson)

}

@RestController
@RequestMapping("/groups")
class GroupController(val service: GroupServiceImpl) {
  @GetMapping fun getGroups(): Iterable<GroupDTO> = service.getGroups()

  @PostMapping fun addGroup(@RequestBody newGroup: NewGroup) = service.addGroup(newGroup)

  /* @PostMapping("{id}/add")
  fun addAttrib(@PathVariable id: UUID, @RequestBody newAttrib: NewAttrib): GroupDTO =
      service.addAttrib(id, newAttrib) */
}

@RestController
@RequestMapping("/person")
class PersonController(val service: PersonServiceImpl) {
  @GetMapping fun getPeople(): Iterable<PersonDTO> = service.getPeople()

  @PostMapping fun addPerson(@RequestBody newPerson: NewPerson) = service.addPerson(newPerson)
}
