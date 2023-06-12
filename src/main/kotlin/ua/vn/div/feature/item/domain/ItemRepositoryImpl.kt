package ua.vn.div.feature.item.domain

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.vn.div.feature.item.data.Item
import ua.vn.div.feature.item.domain.mapper.toDTO
import ua.vn.div.feature.item.domain.model.ItemCreateRequest
import ua.vn.div.feature.item.domain.model.ItemDTO
import ua.vn.div.feature.item.domain.model.ItemUpdateRequest

class ItemRepositoryImpl : ItemRepository {
    override suspend fun getAllItems(): List<ItemDTO> {
        val items = newSuspendedTransaction {
            return@newSuspendedTransaction Item.all().sortedBy { i -> i.id }.toList()
        }
        return items.map { i -> i.toDTO() }
    }

    override suspend fun getItem(id: Int): ItemDTO? {
         return newSuspendedTransaction {
            return@newSuspendedTransaction Item.findById(id)?.toDTO()
        }
    }

    override suspend fun createItem(itemCreateRequest: ItemCreateRequest): ItemDTO {
        return newSuspendedTransaction {
            return@newSuspendedTransaction Item.new {
                name = itemCreateRequest.name
                description = itemCreateRequest.description
                stock = itemCreateRequest.stock
                price = itemCreateRequest.price
                isActive = itemCreateRequest.isActive
                imageUrl = itemCreateRequest.imageUrl
            }.toDTO()
        }
    }

    override suspend fun updateItem(id: Int, request: ItemUpdateRequest): ItemDTO? {
        return newSuspendedTransaction {
            return@newSuspendedTransaction Item.findById(id)?.apply {
                name = request.name
                description = request.description
                stock = request.stock
                price = request.price
                isActive = request.isActive
                imageUrl = request.imageUrl
            }?.toDTO()
        }
    }

    override suspend fun removeItem(id: Int): ItemDTO? {
        return newSuspendedTransaction {
            val dto = Item.findById(id)?.toDTO()
            Item.findById(id)?.delete()
            return@newSuspendedTransaction dto
        }
    }
}