package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.products.*
import java.util.UUID

interface AlbumService {
  fun getAlbums(): Iterable<AlbumDTO>

  fun addAlbum(newProduct: NewProduct): AlbumDTO
  fun createAlbum(newProduct: NewProduct): Album

  // fun updateAlbum(id: UUID, editProduct: EditProduct): ProductDTO
}
