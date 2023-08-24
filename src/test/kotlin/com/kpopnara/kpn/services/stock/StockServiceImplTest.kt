package com.kpopnara.kpn.services.stock

import com.kpopnara.kpn.models.products.Product
import com.kpopnara.kpn.models.products.ProductType
import com.kpopnara.kpn.models.stock.*
import com.kpopnara.kpn.repos.ProductRepo
import com.kpopnara.kpn.repos.StockRepo
import com.kpopnara.kpn.services.StockServiceImpl
import io.mockk.*
import org.junit.Ignore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.test.assertFailsWith

class StockServiceImplTest {

    val stockRepository : StockRepo<Stock> = mockk();
    val productRepository : ProductRepo<Product> = mockk();

    val stockService = StockServiceImpl(stockRepository, productRepository)

    val product1 = Product(
        UUID.randomUUID(),
        ProductType.ASSET,
        "Eric Lightstick",
        "description1",
        "gtin1",
        10.5,
        emptySet()
    )

    val stock1 = Stock(
        UUID.randomUUID(),
        LocationType.CHI_SOUTHLOOP,
                product1,
        false,
        0,
        5,
        "2019-08-07 09:00:00",
        true,
        "2019-08-13 12:00:00",
        "9400 1000 0000 0000 0000 00"
    )

    val product2 = Product(
        UUID.randomUUID(),
        ProductType.ALBUM,
        "New Jeans Super Shy",
        "description2",
        "gtin2",
        3000.37,
        emptySet()
    )

    val stock2 = Stock(
        UUID.randomUUID(),
        LocationType.MI_SOUTHFIELD,
        product2,
        true,
        3,
        10,
        "2023-08-07 09:00:00",
        true,
        "2023-08-13 12:00:00",
        "5300 1000 0000 0000 0000 00"
    )

    init {
        product1.stock = setOf(stock1)
        product2.stock = setOf(stock2)
    }
    @Test
    fun whenGetStock_thenReturnStock() {
        val randomUUID : UUID = UUID.randomUUID()

        every { stockRepository.findById(randomUUID) } returns Optional.of(stock1)

        val result = stockService.getStock(randomUUID)

        verify(exactly = 1) { stockRepository.findById(randomUUID) };
        assertEquals(stock1.toDTO(), result)
    }

    @Test
    fun whenGetStocks_thenReturnStocks() {
        val stockList = listOf(stock1, stock2)
        val expected = listOf(stock1.toDTO(), stock2.toDTO())

        every { stockRepository.findAll() } returns stockList

        val result = stockService.getStocks()

        verify(exactly = 1) { stockRepository.findAll() };
        assertEquals(expected, result)
    }

    @Test
    fun whenGetStocksAtLocation_thenReturnStocks() {
        val stockList = listOf(stock1)
        val expected = listOf(stock1.toDTO())

        every { stockRepository.findAllByLocation(LocationType.CHI_SOUTHLOOP) } returns stockList

        val result = stockService.getStocksAtLocation("SOUTHLOOP")

        verify(exactly = 1) { stockRepository.findAllByLocation(LocationType.CHI_SOUTHLOOP) };
        assertEquals(expected, result)
    }

    @Test
    fun whenAddStock_thenSaveStock() {
        product1.stock = emptySet()
        val newStock = NewStock(LocationType.CHI_SOUTHLOOP,
            product1.id,
            false,
            0,
            5,
            "2019-08-07 09:00:00",
            true,
            "2019-08-13 12:00:00",
            "9400 1000 0000 0000 0000 00")

        every { product1.id?.let { productRepository.findById(it) } } returns Optional.of(product1)
        every { stockRepository.save(any()) } returns stock1
        val result = stockService.addStock(newStock)

        verify(exactly = 1) { product1.id?.let { productRepository.findById(it) } };
        verify {
            stockRepository.save(withArg {
                assertEquals(it.exclusive, false)
                assertEquals(it.count, 0)
                assertEquals(it.restock_threshold, 5)
                assertEquals(it.oos_date, "2019-08-07 09:00:00")
                assertEquals(it.ordered, true)
                assertEquals(it.order_date, "2019-08-13 12:00:00")
                assertEquals(it.tracking, "9400 1000 0000 0000 0000 00")
            })
        }
        assertEquals(stock1.toDTO(), result)
    }

    @Test
    fun whenAddStockWhenStockExists_thenReturnConflictError() {
        val newStock = NewStock(LocationType.CHI_SOUTHLOOP,
            product1.id,
            false,
            0,
            5,
            "2019-08-07 09:00:00",
            true,
            "2019-08-13 12:00:00",
            "9400 1000 0000 0000 0000 00")

        every { product1.id?.let { productRepository.findById(it) } } returns Optional.of(product1)
        every { stockRepository.save(any()) } returns stock1

        assertFailsWith(
            exceptionClass = ResponseStatusException(HttpStatus.CONFLICT)::class,
            block = {
                stockService.addStock(newStock)
            }
        )
    }

    @Test
    fun whenUpdateStock_thenSaveStock() {
        val editStock = EditStock(
            true,
            3,
            10,
            "2023-08-07 09:00:00",
            true,
            "2023-08-13 12:00:00",
            "5300 1000 0000 0000 0000 00")

        every { stockRepository.findById(any()) } returns Optional.of(stock1)
        every { stockRepository.save(any()) } returns stock2

        val result = product1.id?.let { stockService.updateStock(it, editStock) }

        verify(exactly = 1) { product1.id?.let { stockRepository.findById(it) } };
        verify {
            stockRepository.save(withArg {
                assertEquals(it.exclusive, true)
                assertEquals(it.count, 3)
                assertEquals(it.restock_threshold, 10)
                assertEquals(it.oos_date, "2023-08-07 09:00:00")
                assertEquals(it.ordered, true)
                assertEquals(it.order_date, "2023-08-13 12:00:00")
                assertEquals(it.tracking, "5300 1000 0000 0000 0000 00")
            })
        }
        assertEquals(stock2.toDTO(), result)
    }

    @Test
    fun whenDeleteStock_thenDeleteStock() {

        assertEquals(stock1.product.stock.size, 1)

        every { stockRepository.findById(any()) } returns Optional.of(stock1)
        every { stockRepository.deleteById(any()) } just runs

        val result = product1.id?.let { stockService.deleteStock(it) }

        verify {
            stockRepository.deleteById(withArg {
                assertEquals(it, product1.id)
            })
        }

        assertEquals(stock1.product.stock.size, 0)
    }
}