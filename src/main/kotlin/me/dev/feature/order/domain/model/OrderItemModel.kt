package me.dev.feature.order.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDTO(
    val itemId: Int,
    val itemAmount: Int,
    val orderUuid: String,
)

@Serializable class OrderItemInfo(
    val itemId: Int,
    val itemAmount: Int,
    val name: String,
    val imageUrl: String,
)

