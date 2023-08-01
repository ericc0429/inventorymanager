package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.services.ProductServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(val service: ProductServiceImpl) {
  @GetMapping fun getProducts(): Iterable<ProductDTO> = service.getProducts()
  @GetMapping("/albums") fun getAlbums(): Iterable<AlbumDTO> = service.getAlbums()
  @GetMapping("/assets") fun getAssets(): Iterable<AssetDTO> = service.getAssets()
  @GetMapping("/items") fun getItems(): Iterable<ProductDTO> = service.getItems()

  @PostMapping fun addProduct(@RequestBody newProduct: NewProduct): ProductDTO = service.addProduct(newProduct)
  @PostMapping("/albums") fun addAlbum(@RequestBody newProduct: NewProduct): AlbumDTO = service.addAlbum(newProduct)
  @PostMapping("/assets") fun addAsset(@RequestBody newProduct: NewProduct): AssetDTO = service.addAsset(newProduct)
  @PostMapping("/items") fun addItem(@RequestBody newProduct: NewProduct): ProductDTO = service.addItem(newProduct)

}