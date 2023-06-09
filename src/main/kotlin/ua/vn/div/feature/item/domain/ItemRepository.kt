package ua.vn.div.feature.item.domain

import ua.vn.div.feature.item.data.Item
import ua.vn.div.feature.item.domain.model.ItemCreateRequest
import ua.vn.div.feature.item.domain.model.ItemDTO
import ua.vn.div.feature.item.domain.model.ItemUpdateRequest

interface ItemRepository {

    suspend fun getAllItems(): List<ItemDTO>
    suspend fun getItem(id: Int): ItemDTO?
    suspend fun createItem(itemCreateRequest: ItemCreateRequest): ItemDTO
    suspend fun updateItem(id: Int, request: ItemUpdateRequest): ItemDTO?
    suspend fun removeItem(id: Int): ItemDTO?
}