package com.example.exam_chat.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_chat.R
import com.example.exam_chat.databinding.MessageItemBinding
import com.example.exam_chat.databinding.UserItemBinding
import com.example.exam_chat.models.Message
import com.example.exam_chat.models.User
import com.example.exam_chat.utils.Constants

class UsersListAdapter() :
    RecyclerView.Adapter<UsersListAdapter.UsersListAdapterViewHolder>() {



    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id && oldItem.is_online == newItem.is_online
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListAdapterViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersListAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersListAdapterViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount() = differ.currentList.size



    class UsersListAdapterViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
           if(user.name == Constants.LOGIN_VALUE)
               binding.name.text = "me"
            else binding.name.text = user.name
            if(user.is_online == 0)
                binding.isOnline.setImageResource(R.drawable.offline_icon)
        }
    }
}

