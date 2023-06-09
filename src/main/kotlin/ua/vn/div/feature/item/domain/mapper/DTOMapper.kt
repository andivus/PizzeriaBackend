package ua.vn.div.feature.item.domain.mapper

import ua.vn.div.feature.item.data.Item
import ua.vn.div.feature.item.domain.model.ItemDTO

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