package com.example.exam_chat.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.exam_chat.MainActivity
import com.example.exam_chat.R
import com.example.exam_chat.databinding.FragmentLoginBinding
import com.example.exam_chat.factory.MyViewModelFactory
import com.example.exam_chat.models.UserLogModel
import com.example.exam_chat.models.UserModel
import com.example.exam_chat.resourse.Resourse
import com.example.exam_chat.utils.Constants
import com.example.exam_chat.viewmodels.MainViewModel

class LoginFragment : Fragment() {

    private lateinit var binding:FragmentLoginBinding
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var sharedPreferences:SharedPreferences;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE)
        checkAuth()

        viewModel.user.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resourse.Success -> {
                    Toast.makeText(requireContext(), "login success", Toast.LENGTH_SHORT).show()
                    Log.d("check", response.data.toString())
                    sharedPreferences.edit()
                        .putInt(Constants.USER_ID, response.data?.id!!)
                        .putString(Constants.TOKEN, response.data?.token)
                        .putString(Constants.LOGIN, response.data?.name)
                        .apply()
                    Constants.TOKEN_VALUE = response.data?.token.toString()
                    Constants.LOGIN_VALUE = response.data?.name.toString()
                    Constants.USER_ID_VALUE = response.data?.id!!
                    Log.d("check_work", Constants.TOKEN_VALUE)
                    findNavController().navigate(R.id.action_loginFragment_to_chatFragment)
                }
                is Resourse.Loading -> {

                }
                is Resourse.Error -> {
                    Toast.makeText(requireContext(), "login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnToRegistr.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrFragment2)
        }
        binding.btnSubmit.setOnClickListener {
            viewModel.login(UserLogModel(
                binding.login.editText!!.text.toString(),
                binding.password.editText!!.text.toString()
            ))
        }
    }

    private fun checkAuth(){
        if(sharedPreferences.contains(Constants.TOKEN)){
            Constants.LOGIN_VALUE = sharedPreferences.getString(Constants.LOGIN,"").toString()
            Constants.USER_ID_VALUE = sharedPreferences.getInt(Constants.USER_ID,-1)
            Constants.TOKEN_VALUE = sharedPreferences.getString(Constants.TOKEN,"").toString()
            viewModel.getUserById()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }


}