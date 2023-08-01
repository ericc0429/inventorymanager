package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.stock.*
import com.kpopnara.kpn.services.StockServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController(val service: StockServiceImpl) {
  @GetMapping fun getStocks(): Iterable<StockDTO> = service.getStocks()

  @GetMapping("/{location}") fun getStockAtLocation(@PathVariable location: LocationType): Iterable<StockDTO> = service.getStockAtLocation(location)

  @PostMapping fun addStock(@RequestBody newStock: NewStock) = service.addStock(newStock)

  @PutMapping("/{id}") fun updateStock(@PathVariable id: UUID, editStock: EditStock): StockDTO = service.updateStock(id, editStock)

  @DeleteMapping("/{id}") fun deleteStock(@PathVariable id: UUID) : StockDTO = service.deleteStock(id)
}
