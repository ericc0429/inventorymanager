package com.kpopnara.kpn.models

import com.kpopnara.kpn.models.OAuthHandler.AuthorizeHandler
import com.kpopnara.kpn.*
import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.*
import com.sun.net.httpserver.HttpServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*
import kotlin.collections.ArrayList

@Service
class SquareService(@Autowired private val stockRepository : StockRepo, @Autowired private val assetRepository : AssetRepo, @Autowired private val albumRepository : AlbumRepo) {
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

        connectToSquareClient()

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

        convertInventoryCountToStock(countsList)
    }

    /* This helper method goes through the list of InventoryCounts returned from Square's Batch retrieve inventory counts
     * Inventory API call. Then, it updates or creates the stock data.
     */
    fun convertInventoryCountToStock(inventoryCountList : List<InventoryCount>) {
        var itemsToBeCreatedIdList = ArrayList<String>()
        for (inventoryCount in inventoryCountList) {
            val catalogId = inventoryCount.catalogObjectId
            val existingAlbum = albumRepository.findByCatalogId(catalogId)
            val existingAsset = assetRepository.findByCatalogId(catalogId)
            if (existingAlbum == null && existingAsset == null) {
                itemsToBeCreatedIdList.add(catalogId)
            } else {
                println("No album or asset found")
                val existingStock = stockRepository.findByCatalogId(inventoryCount.catalogObjectId)
                // TODO: Need more logical solution to separate existingAsset & existingAlbum with a better error handling
                if (existingStock == null) {
                    var newStock : Stock
                    if (existingAlbum != null) {
                        newStock = Stock(LocationType.SOUTHLOOP_CHI,
                                            existingAlbum,
                                            Integer.parseInt(inventoryCount.quantity),
                                            0,
                                            "",
                                            false,
                                            "",
                                            "",
                                            catalogId)
                        println("Found matching stock and updating")
                        stockRepository.save(newStock)
                    } else if (existingAsset != null) {
                        newStock = Stock(LocationType.SOUTHLOOP_CHI,
                                            existingAsset,
                                            Integer.parseInt(inventoryCount.quantity),
                                            0,
                                            "",
                                            false,
                                            "",
                                            null,
                                            catalogId)
                        println("Found matching stock and updating")
                        stockRepository.save(newStock)
                    }

                } else {
                    println("Found no matching stock and creating a new entity in the Stock schema")
                    existingStock.count = Integer.parseInt(inventoryCount.quantity)
                    stockRepository.save(existingStock)
                }

            }
        }

        println("Size of itemsToBeCreatedIdList: " + itemsToBeCreatedIdList.size)
        val batchRetrieveCatalogApi = client?.catalogApi
        val body = BatchRetrieveCatalogObjectsRequest.Builder(itemsToBeCreatedIdList).build()
        val result = batchRetrieveCatalogApi?.batchRetrieveCatalogObjects(body)

        if (result != null) {
            updateItems(result.objects)
        }
    }

    fun updateItems(catalogObjectsList : List<CatalogObject>) {
        for (catalogObject in catalogObjectsList) {
            val itemData = catalogObject.itemData
            val name = itemData.name
            println("Item: " + name + ", type: " + catalogObject.type)
        }
    }
}
