package com.example.exam_chat.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exam_chat.repository.Repository
import com.example.exam_chat.viewmodels.MainViewModel

class MyViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {

//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//
//
//
//        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            MainViewModel(this.repository) as T
//        } else {
//            throw IllegalArgumentException("ViewModel Not Found")
//        }
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(this.repository) as T
    }
}


