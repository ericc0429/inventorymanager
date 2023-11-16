package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.stock.*
import com.kpopnara.kpn.services.StockServiceImpl
import java.util.UUID
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stock")
class StockController(val service: StockServiceImpl) {
  @GetMapping fun getStocks(): Iterable<StockDTO> = service.getStocks()
  @GetMapping("/{id}") fun getStockById(@PathVariable id: UUID): StockDTO = service.getStockById(id)

  @GetMapping("/location/{location}") fun getStockAtLocation(@PathVariable location: String): Iterable<StockDTO> = service.getStocksAtLocation(location)

  @PostMapping fun addStock(@RequestBody newStock: NewStock) = service.addStock(newStock)

  @PutMapping("/{id}") fun updateStock(@PathVariable id: UUID, @RequestBody editStock: EditStock): StockDTO =
    service.updateStock(id, editStock)

  @DeleteMapping("/{id}") fun deleteStock(@PathVariable id: UUID) : StockDTO = service.deleteStock(id)
}
