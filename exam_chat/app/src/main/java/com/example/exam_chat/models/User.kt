package com.example.exam_chat.models

data class User(
    val created_at: String,
    val id: Int,
    val is_online: Int,
    val name: String,
    val updated_at: String
)