package com.kpopnara.kpn.services

import com.kpopnara.kpn.models.stock.*
import java.util.UUID

interface StockService {
  fun getStocks(): Iterable<StockDTO>

  fun getStockAtLocation(location: LocationType): Iterable<StockDTO>

  fun addStock(newStock: NewStock): StockDTO

  fun updateStock(id: UUID, editStock: EditStock): StockDTO

  fun deleteStock(id: UUID): StockDTO
}