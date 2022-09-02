package com.example.exam_chat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.exam_chat.databinding.ActivityMainBinding
import com.example.exam_chat.factory.MyViewModelFactory
import com.example.exam_chat.repository.Repository
import com.example.exam_chat.utils.Constants
import com.example.exam_chat.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var repository = Repository()
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(repository)
        )[MainViewModel::class.java]

    }

    override fun onResume() {
        super.onResume()
        Log.d("resume", Constants.TOKEN_VALUE)
        Log.d("resume", Constants.LOGIN_VALUE)
        Log.d("resume", "${viewModel == null}")
        viewModel.changeOnline(1)
    }

    override fun onStop() {
        Log.d("stop", "it is work")
        if(viewModel.messages.value?.data?.messages?.isNotEmpty() == true){
            val settings = getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE)
            settings.edit()
                .putInt(Constants.LAST_MESSAGE, viewModel.messages.value!!.data!!.messages.lastIndex)
                .apply()
        }
        Log.d("stop", "${Constants.LOGIN_VALUE} ${Constants.TOKEN_VALUE}")
        viewModel.changeOnline(0)
        super.onStop()
        Log.d("stop", "it is work")
    }

    override fun onDestroy() {
        Log.d("stop", "it is work")
        if(viewModel.messages.value?.data?.messages?.isNotEmpty() == true){
            val settings = getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE)
            settings.edit()
                .putInt(Constants.LAST_MESSAGE, viewModel.messages.value!!.data!!.messages.lastIndex)
                .apply()
        }
        Log.d("stop", "${Constants.LOGIN_VALUE} ${Constants.TOKEN_VALUE}")
        viewModel.changeOnline(0)
        super.onDestroy()
    }

}