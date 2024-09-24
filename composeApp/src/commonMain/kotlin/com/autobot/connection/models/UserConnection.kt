package com.autobot.connection.models



data class UserConnection(
    val id: String,
    val connections: List<ConnectionPermissions>
)
