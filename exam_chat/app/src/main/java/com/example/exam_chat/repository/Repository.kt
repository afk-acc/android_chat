package com.example.exam_chat.repository

import com.example.exam_chat.models.UserLogModel
import com.example.exam_chat.models.UserRegModel
import com.example.exam_chat.server.RetrofitService

class Repository constructor() {
    private val retrofitService: RetrofitService = RetrofitService.getInstance()
    suspend fun registration(userRegModel: UserRegModel) = retrofitService.registration(userRegModel)
    suspend fun login(userLogModel: UserLogModel) = retrofitService.login(userLogModel)
    suspend fun getUsers(token:String) = retrofitService.getUsers(token)
    suspend fun logout(user_id:Int, token:String) = retrofitService.logout(token, user_id)
    suspend fun getUserById(id:Int, headerToken:String, token:String) = retrofitService.getUserById(id,headerToken, token)
    suspend fun getMessages(start_id:Int, token: String) = retrofitService.getMessages(start_id, token)
    suspend fun sendMessage(name:String, message:String,token: String) = retrofitService.sendMessage(name,message, token)
    suspend fun changeStatus(name: String, newStatus:Int, token: String) = retrofitService.changeUserStatus(token, name, newStatus)
}