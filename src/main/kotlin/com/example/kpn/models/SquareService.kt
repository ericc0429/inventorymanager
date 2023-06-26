package com.example.kpn.models

import com.example.kpn.models.OAuthHandler.AuthorizeHandler
import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.BatchRetrieveInventoryCountsRequest
import com.sun.net.httpserver.HttpServer
import java.io.IOException
import java.net.InetSocketAddress

class SquareService {
    var client: SquareClient? = null
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
            .accessToken("ACCESS_TOKEN")
            .build()
    }

    @Throws(IOException::class, ApiException::class)
    fun batchRetrieveInventory() {
        val catalogIds: List<String> = ArrayList()
        val locationIds: List<String> = ArrayList()
        val batchRetrieveInventoryCountsRequest = BatchRetrieveInventoryCountsRequest.Builder()
            .build()
        val inventoryApi = client?.inventoryApi
//        val response = inventoryApi.batchRetrieveInventoryCounts(batchRetrieveInventoryCountsRequest)
        //        inventoryApi.batchRetrieveInventoryCountsAsync(body)
//                .thenAccept(result -> {
//                    System.out.println("Success!");
//                })
//                .exceptionally(exception -> {
//                    System.out.println("Failed to make the request");
//                    System.out.println(String.format("Exception: %s", exception.getMessage()));
//                    return null;
//                });
    }
}
