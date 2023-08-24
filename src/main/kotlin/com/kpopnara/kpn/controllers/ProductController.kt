package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.StockDTO
import com.kpopnara.kpn.services.ProductServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(val service: ProductServiceImpl) {
  @GetMapping fun getProducts(): Iterable<ProductDTO> = service.getProducts()
  @GetMapping("/{id}") fun getProductById(@PathVariable id: UUID): ProductDTO = service.getProductById(id)
  @GetMapping("/stock/{id}") fun getProductStock(@PathVariable id: UUID): Iterable<StockDTO> = service.getProductStock(id)

  @GetMapping("/albums") fun getAlbums(): Iterable<AlbumDTO> = service.getAlbums()
  @GetMapping("/assets") fun getAssets(): Iterable<AssetDTO> = service.getAssets()
  @GetMapping("/items") fun getItems(): Iterable<ProductDTO> = service.getItems()

  @PostMapping fun addProduct(@RequestBody newProduct: NewProduct): ProductDTO = service.addProduct(newProduct)
  @PostMapping("/albums") fun addAlbum(@RequestBody newProduct: NewProduct): AlbumDTO = service.addAlbum(newProduct)
  @PostMapping("/assets") fun addAsset(@RequestBody newProduct: NewProduct): AssetDTO = service.addAsset(newProduct)
  @PostMapping("/items") fun addItem(@RequestBody newProduct: NewProduct): ProductDTO = service.addItem(newProduct)

  @PutMapping("/{id}") fun updateProduct(@PathVariable id: UUID, @RequestBody editProduct: EditProduct) = service.updateProduct(id, editProduct)
  @PutMapping("/albums/{id}") fun updateAlbum (@PathVariable id: UUID, @RequestBody editProduct: EditProduct) = service.editAlbum(id, editProduct)
  @PutMapping("/assets/{id}") fun updateAsset (@PathVariable id: UUID, @RequestBody editProduct: EditProduct) = service.editAsset(id, editProduct)
  @PutMapping("/items/{id}") fun updateItem (@PathVariable id: UUID, @RequestBody editProduct: EditProduct) = service.updateItem(id, editProduct)

  @DeleteMapping("/{id}") fun deleteProduct(@PathVariable id: UUID) = service.deleteProduct(id)
}