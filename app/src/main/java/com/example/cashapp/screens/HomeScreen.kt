package com.example.cashapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cashapp.model.Stock
import com.example.cashapp.viewmodel.StocksViewModel
import com.example.cashapp.viewmodel.ViewState

@Composable
fun HomeScreen(navController: NavController, viewModel: StocksViewModel) {

    LaunchedEffect(Unit) {
        viewModel.start(navController)
        viewModel.getStocks()
    }

    viewModel.state.collectAsState().value.let { state ->
        when (state) {
            is ViewState.Content -> {
                if (state.list.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No stocks found",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        items(state.list) {
                            StocksRow(stock = it)
                        }
                    }
                }
            }

            is ViewState.Error -> {}
            ViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StocksRow(stock: Stock) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Text(text = stock.ticker)
            Text(text = "${stock.quantity} shares")
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "${stock.currentPriceCents} ${stock.currency}"
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
}

@Preview
@Composable
fun StocksRowPreview() {
    StocksRow(stock = Stock("AAPL", "Apple", "USD", quantity = 10, currentPriceCents = 1234234))
}