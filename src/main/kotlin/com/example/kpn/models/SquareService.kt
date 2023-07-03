package com.example.kpn.models

import com.example.kpn.models.OAuthHandler.AuthorizeHandler
import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.BatchRetrieveInventoryCountsRequest
import com.squareup.square.models.BatchRetrieveInventoryCountsResponse
import com.squareup.square.models.InventoryCount
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress

class SquareService {
    var client: SquareClient? = null

    private val SQUARE_ACCESS_TOKEN_ENV_VAR = "EAAAEAMXjxAjK42SK99tHgpCJDIuqLEt9lP0QCGCiXz81QFWEM3_4e4HqXSDjjld"

    fun authorize() {
        try {
            val portNumber = 8000
            val server = HttpServer.create(InetSocketAddress(portNumber), 0)
            server.createContext("/callback", OAuthHandler.CallbackHandler())
            server.createContext("/", AuthorizeHandler())
            server.executor = null
            server.start()
            println("Listening on port $portNumber")
        } catch (e: IOException) {
            println("Server startup failed. Exiting.")
            System.exit(1)
        }
    }

    fun connectToSquareClient() {
        client = SquareClient.Builder()
            .environment(Environment.SANDBOX)
            .accessToken(SQUARE_ACCESS_TOKEN_ENV_VAR)
            .build()
    }

    @Throws(IOException::class, ApiException::class)
    fun batchRetrieveInventory() {
        val catalogIds: List<String> = ArrayList()
        val locationIds: List<String> = ArrayList()
        val body = BatchRetrieveInventoryCountsRequest.Builder()
            .limit(1000)
            .build()
        val inventoryApi = client?.inventoryApi
        var countsList = mutableListOf<InventoryCount>()
        var cursor: String?
        if (inventoryApi != null) {
            var result = inventoryApi.batchRetrieveInventoryCounts(body)
            if (result != null) {
                countsList.addAll(result.counts)
                cursor = result.cursor
                do {
                    val newBody = BatchRetrieveInventoryCountsRequest.Builder()
                        .cursor(cursor)
                        .limit(1000)
                        .build()
                    result = inventoryApi.batchRetrieveInventoryCounts(newBody)
                    countsList.addAll(result.counts)
                    cursor = result.cursor
                } while (cursor != null)
            } else {
                println("Empty result!")
            }
        }

        println("PRINTING INVENTORY LIST with size of ${countsList.size}" )
        for (count in countsList) {
            println("${count.catalogObjectId}: ${count.quantity}")
        }
    }
}
