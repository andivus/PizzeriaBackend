package ua.vn.div.feature.order.data

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.UUID

object OrderTable : UUIDTable(name = "orders") {

    val purchaseDate        = datetime("purchase_date")
    val firstName           = varchar("first_name", 256)
    val lastName            = varchar("last_name", 256)
    val email               = varchar("email", 256)
    val phone               = integer("phone")
    val city                = varchar("city", 256)
    val zipCode             = integer("zip_code")
    val firstAddress        = text("first_address")
    val secondAddress       = text("second_address").nullable()
    val totalPrice          = float("total_price")
    val status              = varchar("status", 24)
    val userIp              = varchar("user_ip", 15).nullable()
    val userAgent           = text("user_agent").nullable()
    //val promocode
    //val userId
}

class Order(id: EntityID<UUID>): Entity<UUID>(id){
    companion object: EntityClass<UUID, Order>(OrderTable)

    var purchaseDate by OrderTable.purchaseDate
    var firstName by OrderTable.firstName
    var lastName by OrderTable.lastName
    var email by OrderTable.email
    var phone by OrderTable.phone
    var city by OrderTable.city
    var zipCode by OrderTable.zipCode
    var firstAddress by OrderTable.firstAddress
    var secondAddress by OrderTable.secondAddress
    var totalPrice by OrderTable.totalPrice
    var status by OrderTable.status
    var userIp by OrderTable.userIp
    var userAgent by OrderTable.userAgent
}