package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.artists.Artist
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.*

import com.kpopnara.kpn.repos.ProductRepo
import java.util.UUID
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
        else if (newProduct.type == ProductType.ASSET) return createAsset(newProduct).toProductDTO()
        else return addItem(newProduct)
    }
    override fun addAlbum(newProduct: NewProduct): AlbumDTO {
        return createAlbum(newProduct).toDTO()
    }
    override fun createAlbum(newProduct: NewProduct): Album {
        return albumRepo.save(
            Album(
                id = null,
                name = newProduct.name,
                gtin = newProduct.gtin,
                sku = newProduct.sku,
                price = newProduct.price,
                stock = emptySet<Stock>(),
                artist = emptySet<Artist>(),
                version = newProduct.version,
                extras = emptySet<Product>(),
                released = newProduct.released,
                discography = newProduct.discography,
                format = newProduct.format,
                color = newProduct.color
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
                description = newProduct.description,
                gtin = newProduct.gtin,
                sku = newProduct.sku,
                price = newProduct.price,
                stock = emptySet<Stock>(),
                artist = emptySet<Artist>(),
                version = newProduct.version,
                extras = emptySet<Product>(),
                released = newProduct.released,
                brand = newProduct.brand
            )
        )
    }
    override fun addItem(newProduct: NewProduct): ProductDTO {
        return itemRepo.save(
            Item(
                id = null,
                type = newProduct.type,
                name = newProduct.name,
                description = newProduct.description,
                gtin = newProduct.gtin,
                sku = newProduct.sku,
                price = newProduct.price,
                stock = emptySet<Stock>()
            )
        ).toDTO()
    }

    override fun updateProduct(id: UUID, editProduct: EditProduct): ProductDTO {
        val product = productRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        if (product is Album) return updateAlbum(id, editProduct).toProductDTO()
        else if (product is Asset) return updateAsset(id, editProduct).toProductDTO()
        else return updateItem(id, editProduct)
    }

    override fun editAlbum(id: UUID, editProduct: EditProduct): AlbumDTO {
        return updateAlbum(id, editProduct).toDTO()
    }
    override fun updateAlbum(id: UUID, editProduct: EditProduct): Album {
        val album = albumRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        // album.type = if (editProduct.type != null) editProduct.type else album.type
        album.name = if (editProduct.name != null) editProduct.name else album.name
        album.description = if (editProduct.description != null) editProduct.description else album.description
        album.gtin = if (editProduct.gtin != null) editProduct.gtin else album.gtin
        album.sku = if (editProduct.sku != null) editProduct.sku else album.sku
        album.price = if (editProduct.price != null) editProduct.price else album.price
        album.version = if (editProduct.version != null) editProduct.version else album.version
        album.released = if (editProduct.released != null) editProduct.released else album.released
        album.discography = if (editProduct.discography != null) editProduct.discography else album.discography
        album.format = if (editProduct.format != null) editProduct.format else album.format
        album.color = if (editProduct.color != null) editProduct.color else album.color

        return albumRepo.save(album)
    }

    override fun editAsset(id: UUID, editProduct: EditProduct): AssetDTO {
        return updateAsset(id, editProduct).toDTO()
    }
    override fun updateAsset(id: UUID, editProduct: EditProduct): Asset {
        val asset = assetRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        // asset.type = if (editProduct.type != null) editProduct.type else asset.type
        asset.name = if (editProduct.name != null) editProduct.name else asset.name
        asset.description = if (editProduct.description != null) editProduct.description else asset.description
        asset.gtin = if (editProduct.gtin != null) editProduct.gtin else asset.gtin
        asset.sku = if (editProduct.sku != null) editProduct.sku else asset.sku
        asset.price = if (editProduct.price != null) editProduct.price else asset.price
        asset.version = if (editProduct.version != null) editProduct.version else asset.version
        asset.released = if (editProduct.released != null) editProduct.released else asset.released
        asset.brand = if (editProduct.brand != null) editProduct.brand else asset.brand

        return assetRepo.save(asset)
    }

    override fun updateItem(id: UUID, editProduct: EditProduct): ProductDTO {
        val item = itemRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }

        item.type = if (editProduct.type != null && (
            editProduct.type != ProductType.ALBUM && editProduct.type != ProductType.ASSET))
            editProduct.type else item.type
        item.name = if (editProduct.name != null) editProduct.name else item.name
        item.description = if (editProduct.description != null) editProduct.description else item.description
        item.gtin = if (editProduct.gtin != null) editProduct.gtin else item.gtin
        item.sku = if (editProduct.sku != null) editProduct.sku else item.sku
        item.price = if (editProduct.price != null) editProduct.price else item.price

        return itemRepo.save(item).toDTO()
    }

    override fun deleteProduct(id: UUID): ProductDTO {
        val product = productRepo.findById(id).orElseThrow{ ResponseStatusException(HttpStatus.NOT_FOUND) }
        productRepo.deleteById(id)
        return if (product is Album) product.toProductDTO()
            else if (product is Asset) product.toProductDTO()
            else (product as Item).toDTO()
    }

}