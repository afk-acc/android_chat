package com.example.exam_chat.models

data class Message(
    val created_at: String,
    val id: Int,
    val message: String,
    val name: String,
    val updated_at: String
)