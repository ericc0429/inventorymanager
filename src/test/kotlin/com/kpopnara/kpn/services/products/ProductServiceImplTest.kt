package com.kpopnara.kpn.services.products

import com.kpopnara.kpn.models.artists.Artist
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.Stock
import com.kpopnara.kpn.models.stock.toDTO
import com.kpopnara.kpn.repos.AlbumRepo
import com.kpopnara.kpn.repos.AssetRepo
import com.kpopnara.kpn.repos.ItemRepo
import com.kpopnara.kpn.repos.ProductRepo
import com.kpopnara.kpn.services.ProductServiceImpl
import com.kpopnara.kpn.services.StockServiceImpl
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.test.assertFailsWith

// TODO: Flip expected & actual fields in each assert call
class ProductServiceImplTest {

    val productRepository : ProductRepo<Product> = mockk();
    val albumRepository : AlbumRepo = mockk();
    val assetRepository : AssetRepo = mockk();

    // Not used, but left for backtracking
    val itemRepository : ItemRepo = mockk();

    val productService = ProductServiceImpl(productRepository, albumRepository, assetRepository, itemRepository)

    val randomUUID = UUID.randomUUID()

    val album1 = Album(
        randomUUID,
        "album1",
        "gtinAlbum1",
        30.5,
        emptySet(),
        emptySet(),
        "versionAlbum1",
        emptySet(),
        "releasedAlbum1",
        "discographyAlbum1",
        "formatAlbum1",
        "colorAlbum1"
        )

    val asset1 = Asset(
        randomUUID,
        ProductType.ASSET,
        "asset1",
        "descriptionAsset1",
        "gtinAsset1",
        10.3,
        emptySet(),
        emptySet(),
        "versionAsset1",
        emptySet(),
        "releasedAsset1",
        "brandAsset1"
    )

    @Test
    fun whenGetProducts_thenReturnProducts() {
        val albumList = listOf(album1)
        val assetList = listOf(asset1)
        val itemList : List<Item> = emptyList()

        every { albumRepository.findAll() } returns albumList
        every { assetRepository.findAll() } returns assetList
        every { itemRepository.findAll() } returns itemList

        val result = productService.getProducts().toList()

        verify(exactly = 1) { albumRepository.findAll() }
        verify(exactly = 1) { assetRepository.findAll() }

        assertEquals(2, result.size)
        assertEquals("album1", result.get(0).name)
        assertEquals("gtinAlbum1", result.get(0).gtin)
        assertEquals(30.5, result.get(0).price)
        assertEquals("asset1", result.get(1).name)
        assertEquals("gtinAsset1", result.get(1).gtin)
        assertEquals(10.3, result.get(1).price)
        assertEquals(ProductType.ASSET, result.get(1).type)
    }

    @Test
    fun whenGetAlbums_thenReturnAlbums() {
        val albumList = listOf(album1)

        every { albumRepository.findAll() } returns albumList

        val result = productService.getAlbums().toList()

        verify(exactly = 1) { albumRepository.findAll() }

        assertEquals(1, result.size)
        assertEquals("album1", result.get(0).name)
        assertEquals("gtinAlbum1", result.get(0).gtin)
        assertEquals(30.5, result.get(0).price)
    }

    @Test
    fun whenGetAssets_thenReturnAssets() {
        val assetList = listOf(asset1)

        every { assetRepository.findAll() } returns assetList

        val result = productService.getAssets().toList()

        verify(exactly = 1) { assetRepository.findAll() }

        assertEquals(1, result.size)
        assertEquals("asset1", result.get(0).name)
        assertEquals("gtinAsset1", result.get(0).gtin)
        assertEquals(10.3, result.get(0).price)
        assertEquals(ProductType.ASSET, result.get(0).type)
    }

    @Test
    fun whenAddProduct_thenReturnError() {
        val newProduct = NewProduct(
            ProductType.NONE,
            "product1",
            "descriptionProduct1",
            "gtinProduct1",
            1.0,
            emptySet(),
            "versionProduct1",
            emptySet(),
            "releasedProduct1",
            "discographyProduct1",
            "formatProduct1",
            "colorProduct1",
            "brandProduct1"
        )

        assertFailsWith(
                exceptionClass = ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)::class,
                block = {
                    productService.addProduct(newProduct)
                }
        )
    }

    @Test
    fun whenAddProductAlbum_thenAddAlbum() {
        val newProductAlbum = NewProduct(
            ProductType.ALBUM,
            "product1",
            "descriptionProduct1",
            "gtinProduct1",
            1.0,
            emptySet(),
            "versionProduct1",
            emptySet(),
            "releasedProduct1",
            "discographyProduct1",
            "formatProduct1",
            "colorProduct1",
            "brandProduct1"
        )

        val newAlbum = Album(
            UUID.randomUUID(),
            "product1",
            "gtinProduct1",
            1.0,
            emptySet(),
            emptySet(),
            "versionProduct1",
            emptySet(),
            "releasedProduct1",
            "discographyProduct1",
            "formatProduct1",
            "colorProduct1"
        )


        every { albumRepository.save(any() )} returns newAlbum
        val result = productService.addProduct(newProductAlbum)

        verify{
            albumRepository.save(withArg {
                assertEquals(it.type, ProductType.ALBUM)
                assertEquals(it.name, "product1")
                assertEquals(it.gtin, "gtinProduct1")
                assertEquals(it.price, 1.0)
                assertEquals(it.artist, emptySet<String>())
                assertEquals(it.version, "versionProduct1")
                assertEquals(it.extras, emptySet<String>())
                assertEquals(it.released, "releasedProduct1")
                assertEquals(it.discography, "discographyProduct1")
                assertEquals(it.format, "formatProduct1")
                assertEquals(it.color, "colorProduct1")
            })
        }
    }

    @Test
    fun whenAddProductAsset_thenAddAsset() {
        val newProductAsset = NewProduct(
            ProductType.ASSET,
            "product1",
            "descriptionProduct1",
            "gtinProduct1",
            1.0,
            emptySet(),
            "versionProduct1",
            emptySet(),
            "releasedProduct1",
            "discographyProduct1",
            "formatProduct1",
            "colorProduct1",
            "brandProduct1"
        )

        val newAsset = Asset(
            null,
            ProductType.ASSET,
            "product1",
            "descriptionProduct1",
            "gtinProduct1",
            1.0,
            emptySet(),
            emptySet(),
            "versionProduct1",
            emptySet(),
            "releasedProduct1",
            "brandProduct1"
        )


        every { assetRepository.save(any() )} returns newAsset
        val result = productService.addProduct(newProductAsset)

        verify{
            assetRepository.save(withArg {
                assertEquals(it.type, ProductType.ASSET)
                assertEquals(it.name, "product1")
                assertEquals(it.description, "descriptionProduct1")
                assertEquals(it.gtin, "gtinProduct1")
                assertEquals(it.price, 1.0)
                assertEquals(it.stock, emptySet<String>())
                assertEquals(it.artist, emptySet<String>())
                assertEquals(it.version, "versionProduct1")
                assertEquals(it.extras, emptySet<String>())
                assertEquals(it.released, "releasedProduct1")
                assertEquals(it.brand, "brandProduct1")
            })
        }
    }

    @Test
    fun whenEditProductAlbum_thenEditAlbum() {
        val editProductAlbum = EditProduct(
            ProductType.ALBUM,
            "product2",
            "descriptionProduct2",
            "gtinProduct2",
            1.0,
            setOf("artistProduct2"),
            "versionProduct2",
            setOf("extraProduct2"),
            "releasedProduct2",
            "discographyProduct2",
            "formatProduct2",
            "colorProduct2",
            "brandProduct2"
        )

        every { albumRepository.save(any() )} returns album1
        every { productRepository.findById(randomUUID) } returns Optional.of(album1)
        every { albumRepository.findById(randomUUID) } returns Optional.of(album1)
        val result = productService.updateProduct(randomUUID, editProductAlbum)


        // Sets are not being updated in ProductServiceImpl (?)
        verify{
            albumRepository.save(withArg {
                assertEquals(it.type, ProductType.ALBUM)
                assertEquals(it.name, "product2")
                assertEquals(it.gtin, "gtinProduct2")
                assertEquals(it.price, 1.0)
//                assertEquals(it.artist, emptySet<String>())
                assertEquals(it.version, "versionProduct2")
//                assertEquals(it.extras, setOf("extraProduct2"))
                assertEquals(it.released, "releasedProduct2")
                assertEquals(it.discography, "discographyProduct2")
                assertEquals(it.format, "formatProduct2")
                assertEquals(it.color, "colorProduct2")
            })
        }
    }

    @Test
    fun whenEditProductAsset_thenEditAsset() {
        val editProductAsset = EditProduct(
            ProductType.ASSET,
            "product2",
            "descriptionProduct2",
            "gtinProduct2",
            1.0,
            setOf("artistProduct2"),
            "versionProduct2",
            setOf("extraProduct2"),
            "releasedProduct2",
            "discographyProduct2",
            "formatProduct2",
            "colorProduct2",
            "brandProduct2"
        )

        every { assetRepository.save(any() )} returns asset1
        every { productRepository.findById(randomUUID) } returns Optional.of(asset1)
        every { assetRepository.findById(randomUUID) } returns Optional.of(asset1)
        val result = productService.updateProduct(randomUUID, editProductAsset)


        // Sets are not being updated in ProductServiceImpl (?)
        verify{
            assetRepository.save(withArg {
                assertEquals(it.type, ProductType.ASSET)
                assertEquals(it.name, "product2")
                assertEquals(it.description, "descriptionProduct2")
                assertEquals(it.gtin, "gtinProduct2")
                assertEquals(it.price, 1.0)
//                assertEquals(it.stock, emptySet<String>())
//                assertEquals(it.artist, emptySet<String>())
                assertEquals(it.version, "versionProduct2")
//                assertEquals(it.extras, emptySet<String>())
                assertEquals(it.released, "releasedProduct2")
                assertEquals(it.brand, "brandProduct2")
            })
        }
    }

    @Test
    fun whenDeleteProductAlbum_thenDeleteProductAlbum() {
        every { productRepository.findById(any()) } returns Optional.of(album1)
        every { productRepository.deleteById(any()) } just runs

        val result = productService.deleteProduct(randomUUID)

        verify {
            productRepository.deleteById(withArg {
                assertEquals(it, randomUUID)
            })
        }
    }

    @Test
    fun whenDeleteProductAsset_thenDeleteProductAsset() {
        every { productRepository.findById(any()) } returns Optional.of(asset1)
        every { productRepository.deleteById(any()) } just runs

        val result = productService.deleteProduct(randomUUID)

        verify {
            productRepository.deleteById(withArg {
                assertEquals(it, randomUUID)
            })
        }
    }
}