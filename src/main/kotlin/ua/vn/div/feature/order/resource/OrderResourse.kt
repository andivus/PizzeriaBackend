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
import ua.vn.div.feature.order.domain.model.OrderUpdateRequest
import ua.vn.div.feature.order.domain.model.OrderUpdateStatusRequest

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

    get<Orders> {
        val result = orderRepository.getAllOrders()
        call.respond(result)
    }

    get<Orders.Id> {
        val result = orderRepository.getOrder(it.id)
        if (result != null) call.respond(HttpStatusCode.OK, result)
        else call.respond(HttpStatusCode.NotFound)
    }

    post<Orders>{
        val request = call.receive<OrderCreateRequest>()
        try {
            val result = orderRepository.createOrder(request)
            if (result == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    put<Orders.Id>{
        val request = call.receive<OrderUpdateRequest>()
        try {
            val result = orderRepository.updateOrder(it.id, request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    put<Orders.Id.Status>{
        val request = call.receive<OrderUpdateStatusRequest>()
        try {
            val result = orderRepository.updateOrderStatus(it.parent.id, request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    get<Orders.Id.Items> {
        val result = orderRepository.getAllOrderItems(it.parent.id)
        call.respond(result)
    }

}