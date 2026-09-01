package com.example.b2bmarketplace.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.b2bmarketplace.data.DemoApiService
import com.example.b2bmarketplace.data.Order
import com.example.b2bmarketplace.data.OrderStatus
import com.example.b2bmarketplace.data.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrdersUiState(
    val isLoading: Boolean = true,
    val orders: List<Order> = emptyList(),
    val query: String = "",
    val selectedStatus: OrderStatus? = null,
    val error: String? = null
)

class OrdersViewModel(
    private val repository: OrdersRepository = OrdersRepository(DemoApiService())
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()
    private var allOrders: List<Order> = emptyList()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.loadOrders()
                .onSuccess { orders ->
                    allOrders = orders
                    applyFilters()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message ?: "Unable to load orders") }
                }
        }
    }

    fun updateQuery(query: String) {
        _uiState.update { it.copy(query = query) }
        applyFilters()
    }

    fun updateStatus(status: OrderStatus?) {
        _uiState.update { it.copy(selectedStatus = status) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        _uiState.update {
            it.copy(
                orders = repository.filter(allOrders, state.query, state.selectedStatus),
                isLoading = false,
            )
        }
    }
}