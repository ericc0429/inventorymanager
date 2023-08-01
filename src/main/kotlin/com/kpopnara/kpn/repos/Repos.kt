package com.kpopnara.kpn.repos

import com.kpopnara.kpn.models.artists.*
import com.kpopnara.kpn.models.products.*
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
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
  fun findByName(name: String) : T?
}

@Repository interface AlbumRepo : ProductRepo<Album> {
  // override fun findByName(name: String) : Album?
}

@Repository interface AssetRepo : ProductRepo<Asset> {
  // override fun findByName(name: String) : Asset?
}

@Repository interface ItemRepo : ProductRepo<Item> {
  // override fun findByName(name: String) : Item?
}