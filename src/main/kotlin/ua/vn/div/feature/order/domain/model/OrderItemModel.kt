package ua.vn.div.feature.order.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemDTO(
    val itemId: Int,
    val orderUuid: String,
    val itemAmount: Int
)

