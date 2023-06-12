package ua.vn.div.feature.order.domain

import ua.vn.div.feature.order.domain.model.*

interface OrderRepository {

    suspend fun getAllOrders(): List<OrderDTO>
    suspend fun getOrder(uuid: String): OrderDTO?
    suspend fun createOrder(request: OrderCreateRequest): OrderDTO?
    suspend fun updateOrder(uuid: String, request: OrderUpdateRequest): OrderDTO?
    suspend fun updateOrderStatus(uuid: String, request: OrderUpdateStatusRequest): OrderDTO?
    suspend fun getAllOrderItems(uuid: String): List<OrderItemDTO>
}