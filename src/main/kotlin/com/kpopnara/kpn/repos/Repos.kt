package com.kpopnara.kpn.repos

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.Stock
import com.kpopnara.kpn.models.stock.LocationType
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable
import org.springframework.stereotype.Repository

@Repository interface ArtistRepo<T : Artist?> : JpaRepository<T, UUID?> {
  fun findByName(name: String) : T?
  fun findByNameContaining(name: String) : T?
}

@Repository interface GroupRepo : ArtistRepo<Group> {
  override fun findByName(name: String) : Group?
  override fun findByNameContaining(name: String) : Group?
}

@Repository interface PersonRepo : ArtistRepo<Person> {
  override fun findByName(name: String) : Person?
  override fun findByNameContaining(name: String) : Person?
}

@Repository interface ProductRepo<T : Product?> : JpaRepository<T, UUID?> {
  @Nullable
  fun findByName(name: String) : T?

  @Nullable
  fun findBySku(sku: String) : Product
}

@Repository interface AlbumRepo : ProductRepo<Album> {
  // override fun findByName(name: String) : Album?
  override fun findBySku(sku: String) : Album
}

@Repository interface AssetRepo : ProductRepo<Asset> {
  // override fun findByName(name: String) : Asset?
  override fun findBySku(sku: String) : Asset
}

@Repository interface StockRepo<T : Stock?> : JpaRepository<T, UUID?> {

  @Nullable
  fun findAllByLocation(location: LocationType) : List<Stock>
  // fun findAllByProduct(productId: UUID) : Stock?

  @Nullable
  fun findByCatalogIdAndLocation(catalogId: String, location : LocationType) : Stock
}