package ua.vn.div.feature.order.resource

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ua.vn.div.feature.order.domain.OrderRepository
import ua.vn.div.feature.order.domain.model.OrderCreateRequest
import ua.vn.div.feature.order.domain.model.OrderItemCreateRequest
import ua.vn.div.feature.order.domain.model.OrderUpdateRequest
import ua.vn.div.feature.order.domain.model.OrderUpdateStatusRequest

@Resource("/orders")
class Orders {
    @Resource("{id}")
    class Order(val parent: Orders, val uuid: String) {

        @Resource("items")
        class Item(val order: Order, val id: String)
    }
}

fun Route.orderEndpoint() {
    val orderRepository by inject<OrderRepository>()

    get<Orders> {
        val result = orderRepository.getAllOrders()
        call.respond(result)
    }

    get<Orders.Order> {
        val result = orderRepository.getOrder(it.uuid)
        if (result != null) call.respond(HttpStatusCode.OK, result)
        else call.respond(HttpStatusCode.NotFound)
    }

    post<Orders>{
        val request = call.receive<OrderCreateRequest>()
        try {
            val result = orderRepository.createOrder(request)
            call.respond(HttpStatusCode.OK, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    put<Orders.Order>{
        val request = call.receive<OrderUpdateRequest>()
        try {
            val result = orderRepository.updateOrder(it.uuid, request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    put<Orders.Order>{
        val request = call.receive<OrderUpdateStatusRequest>()
        try {
            val result = orderRepository.updateOrderStatus(it.uuid, request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get<Orders.Order.Item> {
        val result = orderRepository.getAllOrderItems(it.order.uuid)
        call.respond(result)
    }

    post<Orders.Order.Item> {
        val request = call.receive<OrderItemCreateRequest>()
        try {
            val result = orderRepository.addOrderItem(it.order.uuid, it.id.toInt(), request)
            if (result == null) call.respond(HttpStatusCode.InternalServerError)
            else call.respond(HttpStatusCode.OK, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}