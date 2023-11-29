package com.example.meditation.model

data class Users(
    val id_user: Int,
    val username: String,
    val email: String,
    val password: String,
    val level: Int,
    val created_at: String,
    val updated_at: String,
)

