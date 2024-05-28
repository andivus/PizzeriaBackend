package ua.vn.div.feature.order.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO (
    val uuid: String,
    val purchaseDate: LocalDateTime,
    val firstName: String,
    val email: String,
    val phone: String,
    val city: String,
    val firstAddress: String,
    val totalPrice: Float,
    val status: String,
)

@Serializable
data class OrderCreateRequest(
    val firstName: String,
    val email: String,
    val phone: String,
    val city: String,
    val firstAddress: String,
    val cart: Map<Int, Int>
)

@Serializable
data class OrderCreateResponse(
    val orderDTO: OrderDTO? = null,
    val status: StatusType? = null
) {
    enum class StatusType {
        ITEM_NOT_FOUND,
        NO_ENOUGH_ITEMS,
        CART_IS_EMPTY
    }
}

@Serializable
data class OrderUpdateRequest(
    val firstName: String,
    val email: String,
    val phone: String,
    val city: String,
    val firstAddress: String,
    val totalPrice: Float,
    val status: String,
)

@Serializable
data class OrderUpdateStatusRequest(
    val status: String
)