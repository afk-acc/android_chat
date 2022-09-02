package com.example.exam_chat.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_chat.databinding.MessageItemBinding
import com.example.exam_chat.databinding.MyMessageItemBinding
import com.example.exam_chat.models.Message
import com.example.exam_chat.utils.Constants


class MessageAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    final val MESSAGE_TYPE_IN = 1;
    final val MESSAGE_TYPE_OUT = 2;

    private val differCallback = object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    private class MessageInViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var name: TextView
        fun bind(messageModel: Message) {
            messageText.text = messageModel.message
            name.text = messageModel.name
        }

        init {
            messageText = itemView.findViewById(com.example.exam_chat.R.id.message)
            name = itemView.findViewById(com.example.exam_chat.R.id.name)
        }
    }

    private class MessageOutViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var name: TextView
        fun bind(messageModel: Message) {
            messageText.text = messageModel.message
            name.text = "me"
        }

        init {
            messageText = itemView.findViewById(com.example.exam_chat.R.id.message)
            name = itemView.findViewById(com.example.exam_chat.R.id.name)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MESSAGE_TYPE_IN) {
            val binding =
                MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MessageInViewHolder(binding.root)
        }
        val binding = MyMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageOutViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (item.name == Constants.LOGIN_VALUE)
            (holder as MessageOutViewHolder).bind(item)
        else (holder as MessageInViewHolder).bind(item)

    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].name == Constants.LOGIN_VALUE)
            return MESSAGE_TYPE_OUT;
        return MESSAGE_TYPE_IN;
    }

    override fun getItemCount() = differ.currentList.size


    class MessageAdapterViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(message: Message) {
            binding.message.text = message.message
        }


    }


    class OtherMessageAdapterViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(message: Message) {
            binding.message.text = message.message
            binding.name.text = message.name
        }

    }


}

