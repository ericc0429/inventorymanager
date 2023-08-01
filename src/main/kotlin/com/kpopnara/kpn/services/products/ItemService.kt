package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.products.*
import java.util.UUID

interface ItemService {
  fun getItems(): Iterable<ProductDTO>

  fun addItem(newProduct: NewProduct): ProductDTO

  fun updateItem(id: UUID, editProduct: EditProduct): ProductDTO

  // fun updateItem(id: UUID, editProduct: EditProduct): ProductDTO
}
