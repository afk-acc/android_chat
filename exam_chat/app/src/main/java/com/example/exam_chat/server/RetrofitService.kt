package com.example.exam_chat.server

import com.example.exam_chat.models.*
import com.example.exam_chat.utils.Constants.Companion.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitService {



    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("registration")
    suspend fun registration(
        @Body
        regModel:UserRegModel,
    ) : Response<UserModel>


    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("login")
    suspend fun login(
        @Body
        logModel:UserLogModel,
    ) : Response<UserModel>

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("user-by-id")
    suspend fun getUserById(
        @Query("id")
        id:Int,
        @Header("Authorization")
        header_token:String,
        @Query("token")
        token:String,
        ) : Response<UserModel>

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("users")
    suspend fun getUsers(
        @Header("Authorization")
        token:String
    ) : Response<UsersList>


    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("messages")
    suspend fun getMessages(
        @Query("start_id")
        start_id:Int,
        @Header("Authorization")
        token:String
    ) : Response<MessagesModel>

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("send_message")
    suspend fun sendMessage(
        @Query("name")
        name:String,
        @Query("message")
        message:String,
        @Header("Authorization")
        token:String
    ) : Response<Any>



    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("logout")
    suspend fun logout(
        @Header("Authorization")
        token:String,
        @Query("id")
        user_id:Int
    )


    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("user-status")
    suspend fun changeUserStatus(
        @Header("Authorization")
        token:String,
        @Query("name")
        userName:String,
        @Query("is_online")
        newStatus:Int
    ) : Response<Any>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}