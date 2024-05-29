package me.dev.feature.user.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserTokensTable : IntIdTable(name = "user_tokens") {
    val user =                  reference("user", UserTable)
    val token =                 text("token")
    val validTime =             datetime("valid_time")
}

class UserTokens(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<UserTokens>(UserTokensTable)

    var user by User referencedOn UserTokensTable.user
    var token by UserTokensTable.token
    var validTime by UserTokensTable.validTime
}