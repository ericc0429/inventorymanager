package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.StockDTO
import java.util.UUID

interface ProductService {
  fun getProducts(): Iterable<ProductDTO>
  fun getProductById(id: UUID): ProductDTO

  fun getProductStock(id: UUID): Iterable<StockDTO>

  fun addProduct(newProduct: NewProduct): ProductDTO

  fun updateProduct(id: UUID, editProduct: EditProduct): ProductDTO

  fun deleteProduct(id: UUID): ProductDTO
}