package me.dev.feature.order.resource

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import me.dev.feature.order.domain.OrderRepository
import me.dev.feature.order.domain.model.OrderCreateRequest
import me.dev.feature.order.domain.model.OrderCreateResponse
import me.dev.feature.order.domain.model.OrderUpdateRequest
import me.dev.feature.order.domain.model.OrderUpdateStatusRequest

@Resource("/orders")
class Orders {
    @Resource("{id}")
    class Id(val parent: Orders, val id: String) {
        @Resource("items")
        class Items(val parent: Id)
        @Resource("status")
        class Status(val parent: Id)

    }
}

fun Route.orderEndpoint() {
    val orderRepository by inject<OrderRepository>()

    post<Orders>{
        val request = call.receive<OrderCreateRequest>()
        try {
            val result = orderRepository.createOrder(request)
            if (result.status == OrderCreateResponse.StatusType.NO_ENOUGH_ITEMS) call.respond(HttpStatusCode.NotAcceptable)
            else if (result.status == OrderCreateResponse.StatusType.ITEM_NOT_FOUND) call.respond(HttpStatusCode.BadRequest)
            else if (result.status == OrderCreateResponse.StatusType.CART_IS_EMPTY) call.respond(HttpStatusCode.NoContent)
            else if (result.orderDTO != null) call.respond(HttpStatusCode.OK, result.orderDTO)
            else call.respond(HttpStatusCode.NotAcceptable)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    authenticate("token") {
        get<Orders> {
            val result = orderRepository.getAllOrders()
            call.respond(result)
        }

        get<Orders.Id> {
            val result = orderRepository.getOrder(it.id)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        }

        put<Orders.Id> {
            val request = call.receive<OrderUpdateRequest>()
            try {
                val result = orderRepository.updateOrder(it.id, request)
                if (result != null) call.respond(HttpStatusCode.OK, result)
                else call.respond(HttpStatusCode.NotFound)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        put<Orders.Id.Status> {
            val request = call.receive<OrderUpdateStatusRequest>()
            try {
                val result = orderRepository.updateOrderStatus(it.parent.id, request)
                if (result != null) call.respond(HttpStatusCode.OK, result)
                else call.respond(HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        get<Orders.Id.Items> {
            val result = orderRepository.getAllOrderItems(it.parent.id)
            call.respond(result)
        }
    }

}