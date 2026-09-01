package com.example.b2bmarketplace.data

enum class OrderStatus(val label: String) {
    NEW("New"),
    PROCESSING("Processing"),
    READY("Ready"),
    DELIVERED("Delivered")
}

data class Order(
    val id: String,
    val customer: String,
    val totalCents: Int,
    val itemCount: Int,
    val status: OrderStatus,
    val createdAt: String,
    val priority: Boolean = false
)

fun Int.asRubles(): String = "%,d ₽".format(this / 100).replace(",", " ")