package com.example.cashapp

import com.example.cashapp.model.Stock
import com.example.cashapp.model.StocksResponse
import com.example.cashapp.repository.StocksRepository
import com.example.cashapp.viewmodel.StocksViewModel
import com.example.cashapp.viewmodel.ViewState
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@FlowPreview
@ExperimentalCoroutinesApi
class StocksViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val stocksRepository: StocksRepository = mockk<StocksRepository>()
    private lateinit var viewModel: StocksViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        viewModel = StocksViewModel(stocksRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `getStocks sets state to Content when repository returns stocks`() = testScope.runTest {
        // Arrange
        val mockStocks = listOf(
            Stock(
                ticker = "AAPL",
                name = "Apple",
                currency = "USD",
                currentPriceCents = 100,
                quantity = 10,
                currentPriceTimestamp = 1234567890
            ),
            Stock(
                ticker = "GOOG",
                name = "Google",
                currency = "USD",
                currentPriceCents = 200,
                quantity = 2,
                currentPriceTimestamp = 12567890
            )
        )
        val mockResponse = mockRetrofitResponse(mockStocks)
        coEvery { stocksRepository.getStocks() } returns mockResponse

        assert((viewModel.state.value as ViewState.Content).list.isEmpty())

        // Act
        viewModel.getStocks()

        // Assert
        assert((viewModel.state.value as ViewState.Content).list.size == 2)
        assert((viewModel.state.value as ViewState.Content).list[0].name == "Apple")
        assert((viewModel.state.value as ViewState.Content).list[1].name == "Google")
    }

    @Test
    fun `getStocks sets state to Content when repository returns stocks, but filters stocks with null quantity`() =
        testScope.runTest {
            // Arrange
            val mockStocks = listOf(
                Stock(
                    ticker = "AAPL",
                    name = "Apple",
                    currency = "USD",
                    currentPriceCents = 100,
                    quantity = 10,
                    currentPriceTimestamp = 1234567890
                ),
                Stock(
                    ticker = "GOOG",
                    name = "Google",
                    currency = "USD",
                    currentPriceCents = 200,
                    quantity = null,
                    currentPriceTimestamp = 12567890
                )
            )
            val mockResponse = mockRetrofitResponse(mockStocks)
            coEvery { stocksRepository.getStocks() } returns mockResponse

            assert((viewModel.state.value as ViewState.Content).list.isEmpty())

            // Act
            viewModel.getStocks()

            // Assert
            assert((viewModel.state.value as ViewState.Content).list.size == 1)
            assert((viewModel.state.value as ViewState.Content).list[0].name == "Apple")
        }

    @Test
    fun `getStocks sets state to Content with an empty list`() = testScope.runTest {
        // Arrange
        val mockStocks = emptyList<Stock>()
        val mockResponse = mockRetrofitResponse(mockStocks)
        coEvery { stocksRepository.getStocks() } returns mockResponse

        assert((viewModel.state.value as ViewState.Content).list.isEmpty())

        // Act
        viewModel.getStocks()

        // Assert
        assert((viewModel.state.value as ViewState.Content).list.isEmpty())
    }

    @Test
    fun `getStocks sets state to Error when repository returns error`() = testScope.runTest {
        // Arrange
        coEvery { stocksRepository.getStocks() } returns mockErrorResponse()

        // Act
        viewModel.getStocks()

        // Assert
        assert(viewModel.state.value is ViewState.Error)
        assert((viewModel.state.value as ViewState.Error).message == "Could not get stocks")
    }

    @Test
    fun `serachStocks returns list of stocks that match the name or ticker`() = testScope.runTest {
        // Arrange
        val mockStocks = listOf(
            Stock(
                ticker = "AAPL",
                name = "Apple",
                currency = "USD",
                currentPriceCents = 100,
                quantity = 10,
                currentPriceTimestamp = 1234567890
            ),
            Stock(
                ticker = "GOOG",
                name = "Google",
                currency = "USD",
                currentPriceCents = 200,
                quantity = 1,
                currentPriceTimestamp = 12567890
            )
        )

        val mockResponse = mockRetrofitResponse(mockStocks)
        coEvery { stocksRepository.getStocks() } returns mockResponse

        assert(viewModel.state.value is ViewState.Loading)

        // Act
        viewModel.getStocks()

        // Assert
        assert((viewModel.state.value as ViewState.Content).list.size == 2)
        assert((viewModel.state.value as ViewState.Content).list[0].name == "Apple")

        // Act
        viewModel.searchStocks("GOOG")

        // Assert

        assert((viewModel.state.value as ViewState.Content).list.size == 1)
        assert((viewModel.state.value as ViewState.Content).list[0].name == "Google")
    }

    @Test
    fun `serachStocks returns list of stocks if query is empty`() = testScope.runTest {
        // Arrange
        val mockStocks = listOf(
            Stock(
                ticker = "AAPL",
                name = "Apple",
                currency = "USD",
                currentPriceCents = 100,
                quantity = 10,
                currentPriceTimestamp = 1234567890
            ),
            Stock(
                ticker = "GOOG",
                name = "Google",
                currency = "USD",
                currentPriceCents = 200,
                quantity = 1,
                currentPriceTimestamp = 12567890
            )
        )

        val mockResponse = mockRetrofitResponse(mockStocks)
        coEvery { stocksRepository.getStocks() } returns mockResponse

        assert(viewModel.state.value is ViewState.Loading)

        // Act
        viewModel.getStocks()

        // Assert
        assert((viewModel.state.value as ViewState.Content).list.size == 2)
        assert((viewModel.state.value as ViewState.Content).list[0].name == "Apple")

        // Act
        viewModel.searchStocks("")

        // Assert

        assert((viewModel.state.value as ViewState.Content).list.size == 2)
        assert((viewModel.state.value as ViewState.Content).list[1].name == "Google")
    }

    @Test
    fun `serachStocks returns empty list of stocks when there is no match for the name or ticker`() = testScope.runTest {
        // Arrange
        val mockStocks = listOf(
            Stock(
                ticker = "AAPL",
                name = "Apple",
                currency = "USD",
                currentPriceCents = 100,
                quantity = 10,
                currentPriceTimestamp = 1234567890
            ),
            Stock(
                ticker = "GOOG",
                name = "Google",
                currency = "USD",
                currentPriceCents = 200,
                quantity = 1,
                currentPriceTimestamp = 12567890
            )
        )

        val mockResponse = mockRetrofitResponse(mockStocks)
        coEvery { stocksRepository.getStocks() } returns mockResponse

        assert(viewModel.state.value is ViewState.Loading)

        // Act
        viewModel.getStocks()

        // Assert
        assert((viewModel.state.value as ViewState.Content).list.size == 2)
        assert((viewModel.state.value as ViewState.Content).list[0].name == "Apple")

        // Act
        viewModel.searchStocks("ABCD")

        // Assert

        assert((viewModel.state.value as ViewState.Content).list.isEmpty())
    }

    private fun mockRetrofitResponse(stocks: List<Stock>): Response<StocksResponse> {
        return Response.success(StocksResponse(stocks))
    }

    private fun mockErrorResponse(): Response<StocksResponse> {
        return Response.error(500, "Error".toResponseBody())
    }
}