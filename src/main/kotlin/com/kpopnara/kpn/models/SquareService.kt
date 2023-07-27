package com.kpopnara.kpn.models

import com.google.common.collect.Lists
import com.kpopnara.kpn.*
import com.kpopnara.kpn.models.OAuthHandler.AuthorizeHandler
import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.*
import com.sun.net.httpserver.HttpServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.InetSocketAddress
import java.security.KeyStore
import java.util.*

@Service
class SquareService(@Autowired private val stockRepository : StockRepo,
                    @Autowired private val assetRepository : AssetRepo,
                    @Autowired private val albumRepository : AlbumRepo,
                    @Autowired private val productRepository : ProductRepo) {
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
        var itemVariationsToBeCreatedIdList = ArrayList<String>()

        for (inventoryCount in inventoryCountList) {
            val catalogId = inventoryCount.catalogObjectId
            val existingAlbum = albumRepository.findByCatalogId(catalogId)
            val existingAsset = assetRepository.findByCatalogId(catalogId)
            val existingProduct = productRepository.findByCatalogId(catalogId)
            if (existingAlbum == null && existingAsset == null && existingProduct == null) {
                itemVariationsToBeCreatedIdList.add(catalogId)
//                println("InventoryCount type: " + inventoryCount.catalogObjectType + ", state: " + inventoryCount.state)
                var newStock = Stock(LocationType.SOUTHLOOP_CHI,
                    existingAsset,
                    Integer.parseInt(inventoryCount.quantity),
                    0,
                    "",
                    false,
                    "",
                    "",
                    catalogId)
                stockRepository.save(newStock)
            } else {
                println("Existing album, asset, or product found!")
                val existingStock = stockRepository.findByCatalogId(inventoryCount.catalogObjectId)

                // TODO: Need more logical solution to separate asset, album, and product with a better error handling
                if (existingStock == null) {
                    var newStock = Stock(LocationType.SOUTHLOOP_CHI,
                        existingAsset,
                        Integer.parseInt(inventoryCount.quantity),
                        0,
                        "",
                        false,
                        "",
                        "",
                        catalogId)
                    println("Found matching stock and updating")
                    stockRepository.save(newStock)

                } else {
                    println("Found a matching stock in the DB and updating the values")
                    // TODO: Update more fields when the attributes are finalized
                    existingStock.count = Integer.parseInt(inventoryCount.quantity)
                    stockRepository.save(existingStock)
                }

            }
        }

        updateItems(itemVariationsToBeCreatedIdList)

    }

    fun updateItems(itemVariationsToBeCreatedIdList : List<String>) {
        var categoryMap = HashMap<String, String>()
        var itemsList = ArrayList<CatalogObject>()
        var itemVariationsList = ArrayList<CatalogObject>()

        val batchRetrieveCatalogApi = client?.catalogApi
        val initialListSize = itemVariationsToBeCreatedIdList.size
        println("Size of itemVariationsToBeCreatedIdList: " + itemVariationsToBeCreatedIdList.size)
        val itemVariationPartition: List<List<String>> = Lists.partition(itemVariationsToBeCreatedIdList, 1000)
        var itemsIdList = ArrayList<String>()

        var count = 0
        for (sublist in itemVariationPartition) {
            val body = BatchRetrieveCatalogObjectsRequest.Builder(sublist).objectIds(sublist).includeRelatedObjects(true).build()
            val result : BatchRetrieveCatalogObjectsResponse?
            try {
                result = batchRetrieveCatalogApi?.batchRetrieveCatalogObjects(body)
                if (result != null) {
                    for (catalogObject in result.objects) {
                        count++
                        println("#" + count + " Item variation name: " + catalogObject.itemVariationData.name + " Item type: " + catalogObject.type + ", CatalogId: " + catalogObject.id + ", UPC: " + catalogObject.itemVariationData.upc)
                        itemVariationsList.add(catalogObject)
                    }
                    for (relatedObject in result.relatedObjects) {
                        if (!itemsIdList.contains(relatedObject.id)) {
                            itemsIdList.add(relatedObject.id)
                        }
                    }
                }
            } catch (e : ApiException) {
                for (error in e.errors) {
                    println(error.detail)
                }
            }
        }

        val itemPartition: List<List<String>> = Lists.partition(itemsIdList, 1000)
        count = 0
        for (sublist in itemPartition) {
            val body = BatchRetrieveCatalogObjectsRequest.Builder(sublist).objectIds(sublist).includeRelatedObjects(true).build()
            val result : BatchRetrieveCatalogObjectsResponse?
            try {
                result = batchRetrieveCatalogApi?.batchRetrieveCatalogObjects(body)
                if (result != null) {
                    for (catalogObject in result.objects) {
                        count++
                        println("#" + count + " Item name: " + catalogObject.itemData.name + "     |   categoryID: " + catalogObject.itemData.categoryId + ", CatalogId: " + catalogObject.id + ", Product type: " + catalogObject.itemData.productType)
                        itemsList.add(catalogObject)
                    }
                    for (relatedObject in result.relatedObjects) {
                        if (relatedObject.type.equals("CATEGORY") && !categoryMap.contains(relatedObject.id)) {
                            categoryMap[relatedObject.id] = relatedObject.categoryData.name
                        }
                    }
                }
            } catch (e : ApiException) {
                for (error in e.errors) {
                    println(error.detail)
                }
            }
        }

        for (item in itemsList) {
            val itemData = item.itemData
            val existingStock = stockRepository.findByCatalogId(item.id)
            if (categoryMap.contains(itemData.categoryId) && categoryMap.get(itemData.categoryId).equals("ALBUM")) {
                var newAlbum : Album?
                if (existingStock != null) {
                    newAlbum = Album(
                        itemData.name,
                        "",
                        "",
                        0.0,
                        null,
                        null,
                        null,
                        null,
                        "",
                        "",
                        "",
                        "",
                        item.id
                    )
                } else {
                    newAlbum = Album(
                        itemData.name,
                        "",
                        "",
                        0.0,
                        null,
                        null,
                        null,
                        null,
                        "",
                        "",
                        "",
                        "",
                        item.id
                    )
                }

                albumRepository.save(newAlbum)
                println("SAVING TO ALBUM")
            } else {
                var newAsset = Asset(itemData.name,
                    "",
                    "",
                    0.0,
                    null,
                    null,
                    null,
                    null,
                    "",
                    "",
                    item.id
                )
                assetRepository.save(newAsset)
                println("SAVING TO ASSET")
            }
        }


//        categoryMap.forEach { (key, value) ->
//            println("$key : $value")
//        }

    }

}
