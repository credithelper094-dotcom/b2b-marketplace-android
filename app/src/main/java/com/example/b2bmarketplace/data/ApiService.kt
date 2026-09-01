package com.example.b2bmarketplace.data

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import retrofit2.http.GET

interface ApiService {
    @GET("v1/orders")
    suspend fun fetchOrders(): List<OrderDto>
}

data class OrderDto(
    val id: String,
    val customer: String,
    @SerializedName("total_cents") val totalCents: Int,
    @SerializedName("item_count") val itemCount: Int,
    val status: String,
    @SerializedName("created_at") val createdAt: String,
    val priority: Boolean = false
)

fun OrderDto.toDomain(): Order = Order(
    id = id,
    customer = customer,
    totalCents = totalCents,
    itemCount = itemCount,
    status = OrderStatus.entries.firstOrNull { it.name.equals(status, ignoreCase = true) } ?: OrderStatus.NEW,
    createdAt = createdAt,
    priority = priority,
)

/** Offline source keeps the reference build runnable without a backend. */
class DemoApiService : ApiService {
    override suspend fun fetchOrders(): List<OrderDto> {
        delay(350)
        return listOf(
            OrderDto("ORD-1042", "Northstar Retail", 184_900, 12, "PROCESSING", "Today, 09:42", priority = true),
            OrderDto("ORD-1041", "Morrow & Co.", 72_500, 4, "READY", "Today, 08:18"),
            OrderDto("ORD-1039", "Atlas Supply", 316_000, 28, "NEW", "Yesterday, 16:30", priority = true),
            OrderDto("ORD-1038", "Common Ground", 49_900, 2, "DELIVERED", "Yesterday, 11:05"),
        )
    }
}

object RetrofitApiService {
    fun create(baseUrl: String): ApiService = retrofit2.Retrofit.Builder()
        .baseUrl(baseUrl.ensureTrailingSlash())
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private fun String.ensureTrailingSlash(): String = if (endsWith("/")) this else "$this/"
}