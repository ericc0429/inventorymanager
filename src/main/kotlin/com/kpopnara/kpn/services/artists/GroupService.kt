package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface GroupService {
  fun getGroups(): Iterable<ArtistDTO>

  fun addGroup(newArtist: NewArtist): ArtistDTO

  fun updateGroup(id: UUID, editArtist: EditArtist): ArtistDTO

  fun addPersonToGroup(id: UUID, newMember: Member): ArtistDTO

  fun removePersonFromGroup(id: UUID, removeMember: Member): ArtistDTO
}
