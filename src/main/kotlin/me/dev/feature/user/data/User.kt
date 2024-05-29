package me.dev.feature.user.data

import me.dev.feature.order.data.OrderItemTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object UserTable : UUIDTable(name = "users") {
    val username =                  varchar("username", 16)
    val firstName =              varchar("first_name", 64)
    val secondName =             varchar("second_name", 64)
    val email =                  varchar("email", 64)
    val password =               text("password")

    init{
        index(true, username)
        index(true, email)
    }
}

class User(id: EntityID<UUID>): Entity<UUID>(id){

    companion object: EntityClass<UUID, User>(UserTable)

    var username by UserTable.username
    var firstName by UserTable.firstName
    var secondName by UserTable.secondName
    var email by UserTable.email
    var password by UserTable.password
}