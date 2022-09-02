package com.example.exam_chat.models

data class UserModel(
    val id: Int,
    val is_online: Int,
    val name: String,
    val token: String
)