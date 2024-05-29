package me.dev.feature.order.domain.mapper

import kotlinx.datetime.toKotlinLocalDateTime
import me.dev.feature.item.domain.mapper.toDTO
import me.dev.feature.order.data.Order
import me.dev.feature.order.data.OrderItem
import me.dev.feature.order.domain.model.OrderDTO
import me.dev.feature.order.domain.model.OrderItemDTO

fun Order.toDTO(): OrderDTO {
    return OrderDTO(
        uuid = this.id.toString(),
        purchaseDate = this.purchaseDate.toKotlinLocalDateTime(),
        firstName = this.firstName,
        email = this.email,
        phone = this.phone,
        city = this.city,
        firstAddress = this.firstAddress,
        totalPrice = this.totalPrice,
        status = this.status,
    )
}

fun OrderItem.toDTO(): OrderItemDTO {
    return OrderItemDTO(
        itemId = this.item.id.value,
        orderUuid = this.order.id.value.toString(),
        itemAmount = this.itemAmount,
    )
}