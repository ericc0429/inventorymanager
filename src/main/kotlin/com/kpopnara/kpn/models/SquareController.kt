package com.kpopnara.kpn.models

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/square")
class SquareController(val service: SquareService) {

    @PostMapping("/inventory")
    fun updateInventory() {
        println("checkpoint 1")
        service.batchRetrieveInventory()
    }
}