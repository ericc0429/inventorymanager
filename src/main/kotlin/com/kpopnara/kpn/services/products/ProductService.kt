package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.products.*
import java.util.UUID

interface ProductService {
  fun getProducts(): Iterable<ProductDTO>

  fun addProduct(newProduct: NewProduct): ProductDTO

  fun updateProduct(id: UUID, editProduct: EditProduct): ProductDTO

  fun deleteProduct(id: UUID): ProductDTO
}