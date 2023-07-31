package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface ArtistService {
  fun getAll(): Iterable<ArtistDTO>

  fun deleteArtist(id: UUID): ArtistDTO
}