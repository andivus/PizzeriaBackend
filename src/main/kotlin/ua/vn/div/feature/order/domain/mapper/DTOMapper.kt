package ua.vn.div.feature.order.domain.mapper

import kotlinx.datetime.toKotlinLocalDateTime
import ua.vn.div.feature.item.domain.mapper.toDTO
import ua.vn.div.feature.order.data.Order
import ua.vn.div.feature.order.data.OrderItem
import ua.vn.div.feature.order.domain.model.OrderDTO
import ua.vn.div.feature.order.domain.model.OrderItemDTO

fun Order.toDTO(): OrderDTO {
    return OrderDTO(
        uuid = this.id.toString(),
        purchaseDate = this.purchaseDate.toKotlinLocalDateTime(),
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        phone = this.phone,
        city = this.city,
        zipCode = this.zipCode,
        firstAddress = this.firstAddress,
        secondAddress = this.secondAddress,
        totalPrice = this.totalPrice,
        status = this.status,
        userIp = this.userIp,
        userAgent = this.userAgent
    )
}

fun OrderItem.toDTO(): OrderItemDTO {
    return OrderItemDTO(
        itemId = this.item.value,
        orderUuid = this.order.value.toString(),
        itemAmount = this.itemAmount,
    )
}