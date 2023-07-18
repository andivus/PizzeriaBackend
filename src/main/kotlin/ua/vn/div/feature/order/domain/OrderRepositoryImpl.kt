package ua.vn.div.feature.order.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.vn.div.feature.item.data.Item
import ua.vn.div.feature.order.data.Order
import ua.vn.div.feature.order.data.OrderItem
import ua.vn.div.feature.order.data.OrderItemTable
import ua.vn.div.feature.order.domain.mapper.toDTO
import ua.vn.div.feature.order.domain.model.*
import java.time.LocalDateTime
import java.util.*

class OrderRepositoryImpl : OrderRepository {
    override suspend fun getAllOrders(): List<OrderDTO> {
        val orders = newSuspendedTransaction {
            return@newSuspendedTransaction Order.all().sortedBy { i -> i.purchaseDate }.toList()
        }
        return orders.map { o -> o.toDTO() }
    }

    override suspend fun getOrder(uuid: String): OrderDTO? {
        return newSuspendedTransaction {
            return@newSuspendedTransaction Order.findById(UUID.fromString(uuid))?.toDTO()
        }
    }

    override suspend fun createOrder(request: OrderCreateRequest): OrderCreateResponse {
        return newSuspendedTransaction {

            val cart = request.cart

            var totalPrice = 0f

            for (item in cart) {
                val foundItem = Item.findById(item.key) ?: return@newSuspendedTransaction OrderCreateResponse(status = OrderCreateResponse.StatusType.NOT_FOUND)

                if (!foundItem.isActive) return@newSuspendedTransaction OrderCreateResponse(status = OrderCreateResponse.StatusType.NOT_FOUND)

                if (foundItem.stock < item.value || item.value < 1) return@newSuspendedTransaction OrderCreateResponse(status = OrderCreateResponse.StatusType.NO_ENOUGH_ITEMS)

                totalPrice += item.value * foundItem.price
            }

            val order = Order.new {
                this.purchaseDate = LocalDateTime.now()
                this.firstName = request.firstName
                this.lastName = request.lastName
                this.email = request.email
                this.phone = request.phone
                this.city = request.city
                this.zipCode = request.zipCode
                this.firstAddress = request.firstAddress
                this.secondAddress = request.secondAddress
                this.totalPrice = totalPrice
                this.status = OrderStatusType.UNPAID.toString()
                this.userIp = request.userIp
                this.userAgent = request.userAgent
            }

            for (item in cart) {
                Item.findById(item.key).apply { this!!.stock -= item.value }
                OrderItem.new {
                    this.item = Item.findById(item.key)!!
                    this.order = order
                    this.itemAmount = item.value
                }
            }

            return@newSuspendedTransaction OrderCreateResponse(orderDTO = order.toDTO())
        }
    }

    override suspend fun updateOrder(uuid: String, request: OrderUpdateRequest): OrderDTO? {
        val order = newSuspendedTransaction {
            return@newSuspendedTransaction Order.findById(UUID.fromString(uuid))?.apply {
                this.firstName = request.firstName
                this.lastName = request.lastName
                this.email = request.email
                this.phone = request.phone
                this.city = request.city
                this.zipCode = request.zipCode
                this.firstAddress = request.firstAddress
                this.secondAddress = request.secondAddress
                this.totalPrice = request.totalPrice
                this.status = request.status
            }
        }
        return order?.toDTO()
    }

    override suspend fun updateOrderStatus(uuid: String, request: OrderUpdateStatusRequest): OrderDTO? {
        val order = newSuspendedTransaction {

            try {
                OrderStatusType.valueOf(request.status.uppercase())
            } catch (e: IllegalArgumentException) {
                return@newSuspendedTransaction null
            }

            return@newSuspendedTransaction Order.findById(UUID.fromString(uuid))?.apply {
                this.status = request.status
            }
        }
        return order?.toDTO()
    }

    override suspend fun getAllOrderItems(uuid: String): List<OrderItemDTO> {
        val orderItems = newSuspendedTransaction {
            val order = Order.findById(UUID.fromString(uuid)) ?: return@newSuspendedTransaction null
            val items = OrderItem.find { OrderItemTable.order eq order.id  }.sortedBy { orderItem -> orderItem.id }.toList()
            return@newSuspendedTransaction items.map { i -> i.toDTO() }
        } ?: return emptyList()

        return orderItems
    }
}