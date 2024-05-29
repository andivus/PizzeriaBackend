package me.dev.feature.user.domain.mapper

import me.dev.feature.user.data.User
import me.dev.feature.user.domain.model.UserDTO

fun User.toDTO(): UserDTO {
    return UserDTO(
        uuid = this.id.toString(),
        username = this.username,
        firstName = this.firstName,
        secondName = this.secondName,
        email = this.email,
    )
}