package com.example.b2bmarketplace.data

import kotlinx.coroutines.delay

interface ApiService {
    suspend fun fetchOrders(): List<Order>
}

/** Deterministic local source used by the sample app until a backend is connected. */
class DemoApiService : ApiService {
    override suspend fun fetchOrders(): List<Order> {
        delay(350)
        return listOf(
            Order("ORD-1042", "Northstar Retail", 184_900, 12, OrderStatus.PROCESSING, "Today, 09:42", priority = true),
            Order("ORD-1041", "Morrow & Co.", 72_500, 4, OrderStatus.READY, "Today, 08:18"),
            Order("ORD-1039", "Atlas Supply", 316_000, 28, OrderStatus.NEW, "Yesterday, 16:30", priority = true),
            Order("ORD-1038", "Common Ground", 49_900, 2, OrderStatus.DELIVERED, "Yesterday, 11:05"),
        )
    }
}