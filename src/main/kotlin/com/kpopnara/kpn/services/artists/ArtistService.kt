package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*

interface ArtistService {
  fun getAll(): Iterable<Artist>
}