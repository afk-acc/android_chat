package com.example.exam_chat.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_chat.MainActivity
import com.example.exam_chat.R
import com.example.exam_chat.adapter.MessageAdapter
import com.example.exam_chat.adapter.UsersListAdapter
import com.example.exam_chat.databinding.FragmentChatBinding
import com.example.exam_chat.models.Message
import com.example.exam_chat.resourse.Resourse
import com.example.exam_chat.utils.Constants
import com.example.exam_chat.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    val viewModel by activityViewModels<MainViewModel>()
    lateinit var settings: SharedPreferences
    var last: Int = 0
    lateinit var usersAdapter: UsersListAdapter

    private fun showInfo() {
        val bottomDialog = BottomSheetDialog(requireContext())
        bottomDialog.setContentView(R.layout.header_navigation_drawer)
        bottomDialog.window?.setWindowAnimations(R.style.DialogAnimation);
        bottomDialog.dismissWithAnimation = true;
        val usersRecyclerView =
            bottomDialog.findViewById<RecyclerView>(R.id.user_list)
        usersRecyclerView?.adapter = usersAdapter
        usersRecyclerView?.layoutManager = LinearLayoutManager(requireContext())

        bottomDialog.findViewById<Button>(R.id.btn_logout)?.setOnClickListener {
            Log.d("check_logout", "send")
            viewModel.logout()
            bottomDialog.cancel()
            logout()
        }
        bottomDialog.show()

    }

    private fun logout() {
        Log.d("check_logout", "success")
        if(viewModel.messages.value?.data?.messages?.isNotEmpty() == true){
            val settings = requireContext().getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE)
            settings.edit()
                .putInt(Constants.LAST_MESSAGE, viewModel.messages.value!!.data!!.messages.lastIndex)
                .apply()
        }
        Log.d("stop", "${Constants.LOGIN_VALUE} ${Constants.TOKEN_VALUE}")
        viewModel.changeOnline(0)

        Toast.makeText(requireContext(), "logout success", Toast.LENGTH_SHORT).show()
        settings.edit()
            .putString(Constants.TOKEN, "")
            .putString(Constants.LOGIN, "")
            .putInt(Constants.USER_ID, -1)
            .apply()
        Constants.TOKEN_VALUE = ""
        Constants.USER_ID_VALUE = -1
        Constants.LOGIN_VALUE = ""

        var intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
        Runtime.getRuntime().exit(0)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var adapter = MessageAdapter()
        usersAdapter = UsersListAdapter()
        settings =
            requireContext().getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE)
        last = settings.getInt(Constants.LAST_MESSAGE, 0)
        binding.messageItems.adapter = adapter
        binding.messageItems.layoutManager = LinearLayoutManager(requireContext())
        binding.btnInfo.setOnClickListener {
            showInfo()
        }

        viewModel.logoutMessage.observe(viewLifecycleOwner) { response ->
            logout()
        }

        viewModel.messages.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Resourse.Success -> {
                    Log.d("check", response.data!!.messages.toString())
                    Log.d("last", last.toString())
                    if (response.data!!.messages.isNotEmpty()) {
                        adapter.differ.submitList(response.data!!.messages.toMutableList())
                        if (Constants.IS_OPEN_BEFORE_VALUE) {
                            binding.messageItems.scrollToPosition(last)
                            Constants.IS_OPEN_BEFORE_VALUE = false
                        }
                    }

                }
                is Resourse.Loading -> {

                }
                is Resourse.Error -> {

                    Log.d("check_work", response.message)
//                    findNavController().navigate(R.id.action_chatFragment_to_loginFragment)
                    Log.d("check", response.message)
                    Log.d("check", Constants.TOKEN_VALUE)
                    logout()
                }
            }
        }
        viewModel.getMessages()
        viewModel.usersList.observe(viewLifecycleOwner)
        { response ->
            when (response) {
                is Resourse.Success -> {
                    Log.d("users", response.data!!.users.toString())
                    usersAdapter.differ.submitList(response.data!!.users.toMutableList())
                }
                is Resourse.Loading -> {

                }
                is Resourse.Error -> {
                    logout()
//                    Log.d("check_work", response.message)
//                    settings.edit().clear().apply()
//                    findNavController().navigate(R.id.action_chatFragment_to_loginFragment)
//                    Log.d("check", response.message)
//                    Log.d("check", Constants.TOKEN_VALUE)
                }
            }
        }

        viewModel.getAllUsers()
        binding.btnSendMessage.setOnClickListener {
            if (binding.messageText.editText!!.text.isNotEmpty()) {
                viewModel.sendMessage(binding.messageText.editText!!.text.toString())
                binding.messageText.editText!!.text.clear()
                binding.apply {
                    messageItems.smoothScrollToPosition(messageItems.adapter!!.itemCount)
                }
            } else Toast.makeText(
                requireContext(),
                "Сообщение не может быть пустым",
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.root.setOnClickListener {
            Log.d("check_drawer", "click ${it.id}")
        }
//        val btn = view.findViewById<Button>(R.id.btn_logout);
//        var btn:Button = binding.navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
//        val headerView = binding.navigationView.inflateHeaderView(R.layout.header_navigation_drawer)
//        headerView.findViewById<Button>(R.id.btn_logout).setOnClickListener{
//            Toast.makeText(requireContext(), "logout start", Toast.LENGTH_SHORT).show()
//
//            viewModel.logout(Constants.TOKEN_VALUE, viewModel.user.value!!.data!!.id)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)
        return binding.root
//        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onDestroy() {

        super.onDestroy()
    }


}