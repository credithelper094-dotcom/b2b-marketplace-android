package com.example.b2bmarketplace.data

class Repository(private val apiService: ApiService) {
    suspend fun loadOrders(): Result<List<Order>> = runCatching {
        apiService.fetchOrders()
            .map(OrderDto::toDomain)
            .sortedWith(
                compareByDescending<Order> { it.priority }
                    .thenByDescending { it.createdAt }
            )
    }

    fun filter(orders: List<Order>, query: String, status: OrderStatus?): List<Order> {
        val normalizedQuery = query.trim().lowercase()
        return orders.filter { order ->
            val matchesQuery = normalizedQuery.isBlank() ||
                order.id.lowercase().contains(normalizedQuery) ||
                order.customer.lowercase().contains(normalizedQuery)
            val matchesStatus = status == null || order.status == status
            matchesQuery && matchesStatus
        }
    }
}