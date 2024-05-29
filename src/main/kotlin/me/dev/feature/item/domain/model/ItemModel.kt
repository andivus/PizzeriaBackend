package me.dev.feature.item.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemDTO(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val isActive: Boolean,
    val stock: Int
)

@Serializable
data class ItemCreateRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val isActive: Boolean,
    val stock: Int
)

@Serializable
data class ItemUpdateRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Float,
    val isActive: Boolean,
    val stock: Int
)