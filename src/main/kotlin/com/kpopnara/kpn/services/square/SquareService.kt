package com.kpopnara.kpn.services.square

import com.google.common.collect.Lists
import com.kpopnara.kpn.models.products.*
import com.kpopnara.kpn.models.stock.LocationType
import com.kpopnara.kpn.models.stock.Stock
import com.kpopnara.kpn.repos.ProductRepo
import com.kpopnara.kpn.repos.StockRepo
import com.squareup.square.Environment
import com.squareup.square.SquareClient
import com.squareup.square.exceptions.ApiException
import com.squareup.square.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

@Service
class SquareService(@Autowired private val stockRepository : StockRepo<Stock>,
                    @Autowired private val assetRepository : ProductRepo<Asset>,
                    @Autowired private val albumRepository : ProductRepo<Album>,
                    @Autowired private val productRepository : ProductRepo<Product>
) {
    var client: SquareClient? = null

    // TODO: Improve access token security
    var locationAccessTokenMap : HashMap<LocationType, String> = HashMap<LocationType, String>()
    var parentProductMap: HashMap<String, ParentProduct> = HashMap<String, ParentProduct>()
    var catalogIdProductMap : HashMap<String, Product> = HashMap<String, Product>()
    var categoryMap = HashMap<String, String>()

    init {
        locationAccessTokenMap.put(LocationType.CHI_SOUTHLOOP, "EAAAEAMXjxAjK42SK99tHgpCJDIuqLEt9lP0QCGCiXz81QFWEM3_4e4HqXSDjjld")
//        locationAccessTokenMap.put(LocationType.MI_SOUTHFIELD, "UPDATE ACCESS TOKEN ONCE WE HAVE OWNER PRIVILEGES")
    }

    fun batchRetrieveInventoryAtAllLocations() {

        println("Syncing with Chicago location Square account inventory...")
        locationAccessTokenMap.get(LocationType.CHI_SOUTHLOOP)
            ?.let { connectToSquareClientAtLocation(LocationType.CHI_SOUTHLOOP, it) }
        batchRetrieveInventory(LocationType.CHI_SOUTHLOOP)

        for ((key, value) in locationAccessTokenMap) {
            if (key != LocationType.CHI_SOUTHLOOP) {
                println("Syncing with $key Square account inventory...")
                connectToSquareClientAtLocation(key, value)
                batchRetrieveInventory(key)
            }
        }
    }

    fun connectToSquareClientAtLocation(location : LocationType, accessToken : String) {
        client = SquareClient.Builder()
            .environment(Environment.SANDBOX)
            .accessToken(accessToken)
            .build()
    }

    /* This BatchRetrieveInventoryCountsRequest API retrieves all item variations and their relevant
     * stock information for a specific location.  However, these variations do not include
     * information about the original item info.
     */
    @Throws(IOException::class, ApiException::class)
    fun batchRetrieveInventory(location : LocationType) {

        val inventoryApi = client?.inventoryApi

        val body = BatchRetrieveInventoryCountsRequest.Builder()
            .limit(1000)
            .build()

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

        println("INVENTORY LIST with size of ${countsList.size}" )

        convertInventoryCountToStock(countsList, location)
    }

    /* This helper method goes through the list of item variations, then it determines
     * whether the item and stocks are in our database.
     */
    fun convertInventoryCountToStock(inventoryCountList : List<InventoryCount>, location : LocationType) {
        var itemVariationsToBeCreatedIdList = ArrayList<String>()

        for (inventoryCount in inventoryCountList) {
            val catalogId = inventoryCount.catalogObjectId

            /* If the item variation does not exist in our DB, then put in it in the list to be created later
             * If it exists, then check if the stock matching the item exists in our DB.
             * If the stock exists, then update the stock. If not, then create it and put it in our DB.
             */

            itemVariationsToBeCreatedIdList.add(catalogId)

        }

        updateItems(itemVariationsToBeCreatedIdList, location)
        updateStocks(inventoryCountList, location)
    }

    fun updateItems(itemVariationsToBeCreatedIdList : List<String>, location: LocationType) {
        var itemsList = ArrayList<CatalogObject>()
        var itemVariationsList = ArrayList<CatalogObject>()

        val batchRetrieveCatalogApi = client?.catalogApi
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
                        val pricingAmount : Long?
                        if (catalogObject.itemVariationData.priceMoney != null) {
                            pricingAmount = catalogObject.itemVariationData.priceMoney.amount
                        } else {
                            pricingAmount = 0
                        }
                        println("#" + count + " Item variation name: " + catalogObject.itemVariationData.name + " Pricing: " + pricingAmount + ", CatalogId: " + catalogObject.id + ", SKU: " + catalogObject.itemVariationData.sku)
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


        // Update Items
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
            val categoryName = categoryMap[itemData.categoryId]
            val parentProduct : ParentProduct
            if (categoryName == null) {
                parentProduct = ParentProduct(item.id, itemData.name, "NONE")
            } else {
                parentProduct = ParentProduct(item.id, itemData.name, categoryName)
            }
            if (!parentProductMap.contains(item.id)) {
                parentProductMap.put(item.id, parentProduct)
            }
        }

        for (itemVariation in itemVariationsList) {
            val itemVariationData = itemVariation.itemVariationData
            val parentItemId = itemVariationData.itemId
            val parentProduct = parentProductMap.get(parentItemId)
            if (parentProduct != null) {
                val pricingAmount : Long
                if (itemVariationData.priceMoney != null) {
                    pricingAmount = itemVariationData.priceMoney.amount
                } else {
                    pricingAmount = 0
                }
                 if (parentProduct.categoryName.equals("ALBUM")) {
                     val newAlbum = Album(
                         null,
                         parentProduct.name,
                         "",
                         itemVariationData.sku,
                         pricingAmount.toDouble(),
                         emptySet(),
                         emptySet(),
                        itemVariationData.name,
                         emptySet(),
                         "",
                         "",
                         "",
                         ""
                     )
                     if (catalogIdProductMap.contains(itemVariation.id)) {
                         println("catalogId already exists!")
                         System.exit(-1)
                     }
                     catalogIdProductMap[itemVariation.id] = newAlbum
                     albumRepository.save(newAlbum)
                 } else {
                     val newAsset = Asset(
                         null,
                         ProductType.ASSET,
                         parentProduct.name,
                         "",
                         "",
                         itemVariationData.sku,
                         pricingAmount.toDouble(),
                         emptySet(),
                         emptySet(),
                         itemVariationData.name,
                         emptySet(),
                         "",
                         ""
                     )
                     if (catalogIdProductMap.contains(itemVariation.id)) {
                         println("catalogId already exists!")
                         System.exit(-1)
                     }
                     catalogIdProductMap[itemVariation.id] = newAsset
                     assetRepository.save(newAsset)
                 }
            } else {
                println("No parent product")
                System.exit(-1)
            }
        }
    }

    fun updateStocks(inventoryCountList: List<InventoryCount>, location: LocationType) {
        println("InventoryCountList size: " + inventoryCountList.size)
        for (inventoryCount in inventoryCountList) {
            val product = catalogIdProductMap[inventoryCount.catalogObjectId]
            if (product != null) {
                val newStock = Stock(
                    id = null,
                    location = location,
                    product = product,
                    exclusive = false,
                    count = Integer.parseInt(inventoryCount.quantity),
                    restock_threshold = 0,
                    oos_date = Date(0),
                    ordered = false,
                    order_date = Date(0),
                    tracking = "",
                    catalogId = inventoryCount.catalogObjectId
                    )
                stockRepository.save(newStock)
            } else {
                println("No associated product for the stock")
                System.exit(-1)
            }
        }
    }

}
