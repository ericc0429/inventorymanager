package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.products.*
import java.util.UUID

interface AssetService {
  fun getAssets(): Iterable<AssetDTO>

  fun addAsset(newProduct: NewProduct): AssetDTO
  fun createAsset(newProduct: NewProduct): Asset

  fun editAsset(id: UUID, editProduct: EditProduct): AssetDTO
  fun updateAsset(id: UUID, editProduct: EditProduct): Asset

}
