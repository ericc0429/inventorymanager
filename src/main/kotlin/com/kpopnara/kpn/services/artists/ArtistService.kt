package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface ArtistService {
  fun getAll(): Iterable<Artist>

  fun deleteArtist(id: UUID): Artist
}