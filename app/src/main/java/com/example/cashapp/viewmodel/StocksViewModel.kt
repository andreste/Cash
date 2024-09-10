package com.example.cashapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.cashapp.model.Stock
import com.example.cashapp.repository.StocksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ViewState {
    data object Loading : ViewState()
    data class Content(val list: List<Stock>, val apiList: List<Stock>) : ViewState()
    data class Error(val message: String) : ViewState()
}

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val stocksRepository: StocksRepository
) : ViewModel() {
    private var navController: NavController? = null

    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state = _state.asStateFlow()

    fun start(navController: NavController) {
        this.navController = navController
    }

    fun getStocks() {
        _state.value = ViewState.Loading

        viewModelScope.launch {
            val response = stocksRepository.getStocks()
            if (response.isSuccessful) {
                response.body()?.let {
                    val notNullStocks = it.stocks.filter { it.quantity != null }
                    _state.value = ViewState.Content(notNullStocks, notNullStocks)
                }
            } else {
                _state.value = ViewState.Error("Could not get stocks")
            }
        }
    }

    // search for stock symbol or company name
    // stock.name or stock.ticker
    fun searchStocks(query: String) {
        if (_state.value is ViewState.Content) {
            val internalState = _state.value as ViewState.Content
            if (query.isEmpty()) {
                _state.value = internalState.copy(list = internalState.apiList)
            } else {
                val filteredList = internalState.apiList.filter {
                    it.name == query || it.ticker == query
                }
                _state.value = internalState.copy(list = filteredList)
            }
        }

    }

}