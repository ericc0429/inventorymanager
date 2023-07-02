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

    private val SQUARE_ACCESS_TOKEN_ENV_VAR = "EAAAEIOxkk52IQLldHyQFElLSXSpuGolxuAtGQ_7QxYPfrRng_I2fywYQUDOWgOu"

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
            .build()
        val inventoryApi = client?.inventoryApi
        var counts = emptyList<InventoryCount>()
        if (inventoryApi != null) {
            inventoryApi.batchRetrieveInventoryCountsAsync(body)
                .thenAccept{result: BatchRetrieveInventoryCountsResponse? ->
                    if (result != null) {
                        println("Success!");
                        counts = result.counts
                    }
                }
                .exceptionally{ exception: Throwable ->
                    println("Failed to make the request")
                    println(exception.toString())
                    null
                }
        }

        for (count in counts) {
            println("${count.catalogObjectId}: ${count.quantity}")
        }
    }
}
