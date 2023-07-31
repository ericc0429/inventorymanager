package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.services.ArtistServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/artists")
class ArtistController(val service: ArtistServiceImpl) {
  @GetMapping fun getArtists(): Iterable<ArtistDTO> = service.getArtists()
  @GetMapping("/groups") fun getGroups(): Iterable<ArtistDTO> = service.getGroups()
  @GetMapping("/solo") fun getPeople(): Iterable<ArtistDTO> = service.getPeople()

  @PostMapping fun addArtist(@RequestBody newArtist: NewArtist) = service.addArtist(newArtist)
  @PostMapping("/groups") fun addGroup(@RequestBody newArtist: NewArtist) = service.addGroup(newArtist)
  @PostMapping("/solo") fun addPerson(@RequestBody newArtist: NewArtist) = service.addPerson(newArtist)

  @PutMapping("/groups/{id}")
  fun updateGroup(@PathVariable id: UUID, @RequestBody editArtist: EditArtist) =
  service.updateGroup(id, editArtist)
  @PutMapping("/solo/{id}")
  fun updatePerson(@PathVariable id: UUID, @RequestBody editArtist: EditArtist) =
  service.updatePerson(id, editArtist)

  @PutMapping("/groups/{id}/addMember") fun addPersonToGroup(@PathVariable id: UUID, @RequestBody newMember: Member) = service.addPersonToGroup(id, newMember)
  @PutMapping("/groups/{id}/removeMember") fun removePersonFromGroup(@PathVariable id: UUID, @RequestBody removeMember: Member) = service.removePersonFromGroup(id, removeMember)

  @DeleteMapping("/{id}") fun deleteArtist(@PathVariable id: UUID) = service.deleteArtist(id)
}