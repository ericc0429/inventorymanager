package com.kpopnara.kpn.controllers

import com.kpopnara.kpn.models.stock.LocationType
import com.kpopnara.kpn.services.square.SquareService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/square")
class SquareController(val service: SquareService) {

    @PostMapping("/inventory")
    fun updateInventory() {
        println("Calling updateInventory API...")
        service.batchRetrieveInventoryAtAllLocations()
        println("Finished updateInventory API!")
    }
}