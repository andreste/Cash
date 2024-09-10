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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
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

    /*
    Upon entering a character into this view the view should apply a filter in
    which it only shows stock objects that exactly match the stock symbol or the company name.
    If the list does not match any stock symbol or company name then the list should render an
    empty state indicating the result. An empty state should clearly tell the user that there are
    no matching results. A blank screen or list of empty cells is not valid.
     */

    Column {
        var text by remember { mutableStateOf(TextFieldValue("")) }

        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { newText ->
                text = newText
                viewModel.searchStocks(text.text)
            }
        )

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

                is ViewState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error ${state.message}",
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

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