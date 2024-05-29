package me.dev.feature.item.domain

import me.dev.feature.item.data.Item
import me.dev.feature.item.domain.model.ItemCreateRequest
import me.dev.feature.item.domain.model.ItemDTO
import me.dev.feature.item.domain.model.ItemUpdateRequest

interface ItemRepository {

    suspend fun getAllItems(): List<ItemDTO>
    suspend fun getItem(id: Int): ItemDTO?
    suspend fun createItem(itemCreateRequest: ItemCreateRequest): ItemDTO
    suspend fun updateItem(id: Int, request: ItemUpdateRequest): ItemDTO?
    suspend fun removeItem(id: Int): ItemDTO?
}