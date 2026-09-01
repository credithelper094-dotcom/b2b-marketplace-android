package com.example.b2bmarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.b2bmarketplace.data.Order
import com.example.b2bmarketplace.data.OrderStatus
import com.example.b2bmarketplace.data.asRubles
import com.example.b2bmarketplace.ui.OrdersUiState
import com.example.b2bmarketplace.ui.OrdersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OrdersApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersApp(viewModel: OrdersViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Orders", fontWeight = FontWeight.Bold)
                        Text("B2B Marketplace", style = MaterialTheme.typography.labelSmall)
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh orders")
                    }
                },
            )
        },
    ) { padding ->
        OrdersScreen(
            state = state,
            onQueryChange = viewModel::updateQuery,
            onStatusChange = viewModel::updateStatus,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun OrdersScreen(
    state: OrdersUiState,
    onQueryChange: (String) -> Unit,
    onStatusChange: (OrderStatus?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search orders or customers") },
        )
        Spacer(Modifier.height(12.dp))
        StatusFilters(selected = state.selectedStatus, onSelect = onStatusChange)
        Spacer(Modifier.height(16.dp))
        when {
            state.isLoading -> LoadingState()
            state.error != null -> Text(state.error, color = MaterialTheme.colorScheme.error)
            state.orders.isEmpty() -> EmptyState()
            else -> OrdersList(state.orders)
        }
    }
}

@Composable
private fun StatusFilters(selected: OrderStatus?, onSelect: (OrderStatus?) -> Unit) {
    LazyColumn {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = selected == null, onClick = { onSelect(null) }, label = { Text("All") })
                OrderStatus.entries.forEach { status ->
                    FilterChip(selected = selected == status, onClick = { onSelect(status) }, label = { Text(status.label) })
                }
            }
        }
    }
}

@Composable
private fun OrdersList(orders: List<Order>) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(orders, key = { it.id }) { order -> OrderCard(order) }
    }
}

@Composable
private fun OrderCard(order: Order) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(order.id, fontWeight = FontWeight.Bold)
                        if (order.priority) {
                            Spacer(Modifier.padding(3.dp))
                            Icon(Icons.Default.Star, contentDescription = "Priority", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(order.customer, style = MaterialTheme.typography.bodyMedium)
                }
                Text(order.status.label, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(18.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${order.itemCount} items · ${order.createdAt}", style = MaterialTheme.typography.bodySmall)
                Text(order.totalCents.asRubles(), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(Modifier.height(12.dp))
        Text("Loading orders…", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun EmptyState() {
    Column(Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No orders found", fontWeight = FontWeight.Bold)
        Text("Try another search or status filter.", style = MaterialTheme.typography.bodyMedium)
    }
}