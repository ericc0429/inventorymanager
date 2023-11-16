package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.*
import java.util.UUID

interface ArtistService {
  fun getArtists(): Iterable<ArtistDTO>
  fun getArtistById(id: UUID): ArtistDTO

  fun addArtist(newArtist: NewArtist): ArtistDTO

  // fun updateArtist(editArtist: EditArtist): ArtistDTO
  fun addAlbumToArtist(id: UUID, product: IdNameDTO): ArtistDTO
  fun removeAlbumFromArtist(id: UUID, product: IdNameDTO): ArtistDTO

  fun addAssetToArtist(id: UUID, product: IdNameDTO): ArtistDTO
  fun removeAssetFromArtist(id: UUID, product: IdNameDTO): ArtistDTO

  fun deleteArtist(id: UUID): ArtistDTO
}