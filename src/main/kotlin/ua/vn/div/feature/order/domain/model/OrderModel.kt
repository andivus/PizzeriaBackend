package ua.vn.div.feature.order.domain.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class OrderDTO (
    val uuid: String,
    val purchaseDate: LocalDateTime,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Int,
    val city: String,
    val zipCode: Int,
    val firstAddress: String,
    val secondAddress: String?,
    val totalPrice: Float,
    val status: String,
    val userIp: String?,
    val userAgent: String?
)

@Serializable
data class OrderCreateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Int,
    val city: String,
    val zipCode: Int,
    val firstAddress: String,
    val secondAddress: String?,
    val userIp: String?,
    val userAgent: String?,
    val cart: Map<Int, Int>
)

@Serializable
data class OrderCreateResponse(
    val orderDTO: OrderDTO? = null,
    val status: StatusType? = null
) {
    enum class StatusType {
        NOT_FOUND,
        NO_ENOUGH_ITEMS,
    }
}

@Serializable
data class OrderUpdateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: Int,
    val city: String,
    val zipCode: Int,
    val firstAddress: String,
    val secondAddress: String?,
    val totalPrice: Float,
    val status: String,
)

@Serializable
data class OrderUpdateStatusRequest(
    val status: String
)