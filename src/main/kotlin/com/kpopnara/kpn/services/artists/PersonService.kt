package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface PersonService {
  fun getPeople(): Iterable<ArtistDTO>

  fun addPerson(newArtist: NewArtist): ArtistDTO

  fun updatePerson(id: UUID, editArtist: EditArtist): ArtistDTO

  fun joinGroup(id: UUID, joinGroup: IdNameDTO): ArtistDTO

  fun leaveGroup(id: UUID, leaveGroup: IdNameDTO): ArtistDTO
}
