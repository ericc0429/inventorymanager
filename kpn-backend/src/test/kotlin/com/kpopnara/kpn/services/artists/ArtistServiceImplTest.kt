package com.kpopnara.kpn.services.artists

import com.kpopnara.kpn.models.artists.Artist
import com.kpopnara.kpn.models.products.Product
import com.kpopnara.kpn.repos.*
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArtistServiceImplTest {
    val artistRepository : ArtistRepo<Artist> = mockk();
    val groupRepository : GroupRepo = mockk();
    val personRepository : PersonRepo = mockk();
    val productRepository : ProductRepo<Product> = mockk();
    val albumRepository : AlbumRepo = mockk();
    val assetRepository : AssetRepo = mockk();

    @Test
    fun whenGetArtists_thenReturnArtists() {

    }
}