package com.example.exam_chat.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam_chat.models.*
import com.example.exam_chat.repository.Repository
import com.example.exam_chat.resourse.Resourse
import com.example.exam_chat.utils.Constants
import kotlinx.coroutines.*
import okhttp3.internal.notifyAll
import retrofit2.Response

class MainViewModel constructor(private val mainRepository: Repository) : ViewModel() {

    val user = MutableLiveData<Resourse<UserModel>>()
    val usersList = MutableLiveData<Resourse<UsersList>>()
    val logoutMessage = MutableLiveData<String>()
    val messages = MutableLiveData<Resourse<MessagesModel>>()
    fun registration(userRegModel: UserRegModel) {
        viewModelScope.launch(Dispatchers.IO) {
            user.postValue(Resourse.Loading())
            val response = mainRepository.registration(userRegModel)
            user.postValue(handler(response))
        }
    }
    fun login(userLogModel: UserLogModel) {
        viewModelScope.launch(Dispatchers.IO) {
            user.postValue(Resourse.Loading())
            val response = mainRepository.login(userLogModel)
            user.postValue(handler(response))
        }
    }

    fun getAllUsers(){
        viewModelScope.launch(Dispatchers.IO){
            while (true) {
                usersList.postValue(Resourse.Loading())
                val response = mainRepository.getUsers("Bearer ${Constants.TOKEN_VALUE}")
                usersList.postValue(handler(response))
                delay(2000)
            }
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO){
            mainRepository.logout(Constants.USER_ID_VALUE, "Bearer ${Constants.TOKEN_VALUE}")
            logoutMessage.postValue("success")
        }
    }

    fun getUserById(){
        viewModelScope.launch(Dispatchers.IO) {
            user.postValue(Resourse.Loading())
            val response = mainRepository.getUserById(Constants.USER_ID_VALUE, "Bearer ${Constants.TOKEN_VALUE}", Constants.TOKEN_VALUE);
            user.postValue(handler(response))
        }
    }

    fun getMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            while (true){
                messages.postValue(Resourse.Loading())
                val response = mainRepository.getMessages(0, "Bearer ${Constants.TOKEN_VALUE}");
                messages.postValue(handler(response))
                delay(1000)
            }
        }
    }

    fun sendMessage(message:String){
        viewModelScope.launch(Dispatchers.IO){
            mainRepository.sendMessage(Constants.LOGIN_VALUE, message, "Bearer ${Constants.TOKEN_VALUE}")
        }
    }

    fun changeOnline(value:Int){
        viewModelScope.launch(Dispatchers.IO){
            mainRepository.changeStatus(Constants.LOGIN_VALUE, value, "Bearer ${Constants.TOKEN_VALUE}")
        }
    }

    fun <T> handler(response: Response<T>): Resourse<T> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resourse.Success(it)
            }
        }
        return Resourse.Error(null, response.message())
    }
}