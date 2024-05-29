package me.dev.feature.item.domain.mapper

import me.dev.feature.item.data.Item
import me.dev.feature.item.domain.model.ItemDTO

fun Item.toDTO(): ItemDTO {
    return ItemDTO(
        id = this.id.value,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        stock = this.stock,
        price = this.price,
        isActive = this.isActive
    )
}