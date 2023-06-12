package ua.vn.div.feature.order.domain

import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.vn.div.feature.item.data.Item
import ua.vn.div.feature.order.data.Order
import ua.vn.div.feature.order.data.OrderItem
import ua.vn.div.feature.order.data.OrderItemTable
import ua.vn.div.feature.order.data.OrderTable
import ua.vn.div.feature.order.domain.mapper.toDTO
import ua.vn.div.feature.order.domain.model.*
import ua.vn.div.feature.order.resource.Orders
import java.util.*

class OrderRepositoryImpl : OrderRepository {
    override suspend fun getAllOrders(): List<OrderDTO> {
        val orders = newSuspendedTransaction {
            return@newSuspendedTransaction Order.all().toList()
        }
        return orders.map { o -> o.toDTO() }
    }

    override suspend fun getOrder(uuid: String): OrderDTO? {
        return newSuspendedTransaction {
            return@newSuspendedTransaction Order.findById(UUID.fromString(uuid))?.toDTO()
        }
    }

    override suspend fun createOrder(request: OrderCreateRequest): OrderDTO {
        return newSuspendedTransaction {
            return@newSuspendedTransaction Order.new {
                this.purchaseDate = request.purchaseDate.toJavaLocalDateTime()
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
                this.userIp = request.userIp
                this.userAgent = request.userAgent
            }.toDTO()
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
            return@newSuspendedTransaction Order.findById(UUID.fromString(uuid))?.apply {
                this.status = request.status
            }
        }
        return order?.toDTO()
    }

    override suspend fun getAllOrderItems(uuid: String): List<OrderItemDTO> {
//        val orderItems = newSuspendedTransaction {
//            val items = OrderItem.find { OrderItemTable.order eq uuid }.toList()
//        }
//        return orderItems.map { o -> o.toDTO() }
        TODO("yes")
    }

    override suspend fun addOrderItem(uuid: String, id: Int, request: OrderItemCreateRequest): OrderItemDTO? {
        return newSuspendedTransaction {
            val item = Item.findById(request.orderItemDTO.itemId)
            val order = Order.findById(UUID.fromString(request.orderItemDTO.orderUuid))

            if (item == null || order == null)
                return@newSuspendedTransaction null

            return@newSuspendedTransaction OrderItem.new {
                this.item = item.id
                this.order = order.id
                this.itemAmount = request.orderItemDTO.itemAmount
            }.toDTO()
        }
    }
}