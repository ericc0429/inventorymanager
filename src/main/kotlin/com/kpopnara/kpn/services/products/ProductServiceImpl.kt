package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.Artist
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.*

import com.kpopnara.kpn.repos.ProductRepo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class ProductServiceImpl(
    val productRepo: ProductRepo<Product>,
    val albumRepo: ProductRepo<Album>,
    val assetRepo: ProductRepo<Asset>,
    val itemRepo: ProductRepo<Item>
) : ProductService, AlbumService, AssetService, ItemService {
    override fun getProducts(): Iterable<ProductDTO> {
        return albumRepo.findAll().map() { it.toProductDTO() } +
            assetRepo.findAll().map() { it.toProductDTO() } +
            itemRepo.findAll().map() { it.toDTO() }
    }
    override fun getAlbums(): Iterable<AlbumDTO> {
        return albumRepo.findAll().map() { it.toDTO() }
    }
    override fun getAssets(): Iterable<AssetDTO> {
        return assetRepo.findAll().map() { it.toDTO() }
    }
    override fun getItems(): Iterable<ProductDTO> {
        return itemRepo.findAll().map() { it.toDTO() }
    }
    
    override fun addProduct(newProduct: NewProduct): ProductDTO {
        if (newProduct.type == ProductType.NONE) throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        else if (newProduct.type == ProductType.ALBUM) return createAlbum(newProduct).toProductDTO()
        else return createAsset(newProduct).toProductDTO()
    }
    override fun addAlbum(newProduct: NewProduct): AlbumDTO {
        return createAlbum(newProduct).toDTO()
    }
    override fun createAlbum(newProduct: NewProduct): Album {
        return albumRepo.save(
            Album(
                id = null,
                name = newProduct.name,
                gtin = if (newProduct.gtin != null) newProduct.gtin else "",
                price = if (newProduct.price != null) newProduct.price else 0.0,
                stock = emptySet<Stock>(),
                artist = emptySet<Artist>(),
                version = if (newProduct.version != null) newProduct.version else "",
                extras = emptySet<Product>(),
                released = if (newProduct.released != null) newProduct.released else "",
                discography = if (newProduct.discography != null) newProduct.discography else "",
                format = if (newProduct.format != null) newProduct.format else "",
                color = if (newProduct.color != null) newProduct.color else "",
            )
        )
    }
    override fun addAsset(newProduct: NewProduct): AssetDTO {
        return createAsset(newProduct).toDTO()
    }
    override fun createAsset(newProduct: NewProduct): Asset {
        return assetRepo.save(
            Asset(
                id = null,
                type = newProduct.type,
                name = newProduct.name,
                description = if (newProduct.description != null) newProduct.description else "",
                gtin = if (newProduct.gtin != null) newProduct.gtin else "",
                price = if (newProduct.price != null) newProduct.price else 0.0,
                stock = emptySet<Stock>(),
                artist = emptySet<Artist>(),
                version = if (newProduct.version != null) newProduct.version else "",
                extras = emptySet<Product>(),
                released = if (newProduct.released != null) newProduct.released else "",
                brand = if (newProduct.brand != null) newProduct.brand else "",
            )
        )
    }
    override fun addItem(newProduct: NewProduct): ProductDTO {
        return itemRepo.save(
            Item(
                id = null,
                type = newProduct.type,
                name = newProduct.name,
                description = if (newProduct.description != null) newProduct.description else "",
                gtin = if (newProduct.gtin != null) newProduct.gtin else "",
                price = if (newProduct.price != null) newProduct.price else 0.0,
                stock = emptySet<Stock>(),
            )
        ).toDTO()
    }
}