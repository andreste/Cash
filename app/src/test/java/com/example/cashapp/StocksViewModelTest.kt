package com.example.cashapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StocksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var observer: Observer<StocksViewModel.StocksUiState>

    @Mock
    private lateinit var stockRepository: StockRepository // Mock the repository

    private lateinit var viewModel: StocksViewModel

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        viewModel = StocksViewModel(stockRepository) // Inject the mock repository
        viewModel.uiState.observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `when viewModel is initialized, uiState should be Loading`() = runTest {
        assert(viewModel.uiState.value is StocksViewModel.StocksUiState.Loading)
    }

    @Test
    fun `when fetching stocks is successful, uiState should be Success`() = runTest {
        // Mock successful stock data retrieval
        val stockData = listOf(/* Your stock data objects */)
        // when(stockRepository.getStocks()).thenReturn(stockData)

        viewModel.fetchStocks()

        assert(viewModel.uiState.value is StocksViewModel.StocksUiState.Success)
        // Further assertions to check if the success state contains the correct data
        // val successState = viewModel.uiState.value as StocksViewModel.StocksUiState.Success
        // assertEquals(successState.stocks, stockData)
    }

    @Test
    fun `when fetching stocks fails, uiState should be Error`() = runTest {
        // Mock failed stock data retrieval
        val exception = Exception("Network error")
        // when(stockRepository.getStocks()).thenThrow(exception)

        viewModel.fetchStocks()

        assert(viewModel.uiState.value is StocksViewModel.StocksUiState.Error)
        // Further assertions to check if the error state contains the correct exception
        // val errorState = viewModel.uiState.value as StocksViewModel.StocksUiState.Error
        // assertEquals(errorState.exception, exception)
    }
}