package ua.vn.div.feature.order.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import ua.vn.div.feature.item.data.ItemTable

object OrderItemTable: IntIdTable(name = "order_items"){

    val order = reference("order", OrderTable)
    val item = reference("item", ItemTable)
    val itemAmount = integer("item_amount")

    init{
        index(true, order, item)
    }
}

class OrderItem(id: EntityID<Int>): IntEntity(id){
    companion object: IntEntityClass<OrderItem>(OrderItemTable)
    var order by OrderTable.id
    var item by ItemTable.id
    var itemAmount by OrderItemTable.itemAmount
}