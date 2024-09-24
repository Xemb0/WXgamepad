package com.autobot.connection.models

data class ConnectionPermissions (
    val id:String,
    val name:String,
    val status: Boolean,
    val location :Boolean,
    val profile: Boolean
)

