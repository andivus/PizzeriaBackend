package ua.vn.div.feature.item.resource

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
import ua.vn.div.feature.item.domain.ItemRepository
import ua.vn.div.feature.item.domain.model.ItemCreateRequest
import ua.vn.div.feature.item.domain.model.ItemUpdateRequest

@Resource("/items")
class Items {
    @Resource("{id}")
    class Id(val parent: Items, val id: Int)
}

fun Route.itemEndpoint() {
    val itemRepository by inject<ItemRepository>()

    get<Items>{
        val result = itemRepository.getAllItems()
        call.respond(result)
    }

    get<Items.Id> {
        val result = itemRepository.getItem(it.id)
        if (result != null) call.respond(HttpStatusCode.OK, result)
        else call.respond(HttpStatusCode.NotFound)
    }

    post<Items>{
        val request = call.receive<ItemCreateRequest>()
        try {
            val result = itemRepository.createItem(request)
            call.respond(HttpStatusCode.OK, result)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    put<Items.Id>{
        val request = call.receive<ItemUpdateRequest>()
        try {
            val result = itemRepository.updateItem(it.id, request)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    delete<Items.Id>{
        try {
            val result = itemRepository.removeItem(it.id)
            if (result != null) call.respond(HttpStatusCode.OK, result)
            else call.respond(HttpStatusCode.NotFound)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}