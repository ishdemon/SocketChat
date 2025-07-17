package com.ishdemon.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishdemon.chatapp.data.ChatMessage
import com.ishdemon.chatapp.databinding.ItemChatBinding
import com.ishdemon.chatapp.databinding.ItemChatLeftBinding
import com.ishdemon.chatapp.databinding.ItemChatRightBinding

class ChatAdapter(
    val userId: String
) : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 1) {
            val binding = ItemChatRightBinding.inflate(inflater, parent, false)
            ChatViewHolder.Right(binding)
        } else {
            val binding = ItemChatLeftBinding.inflate(inflater, parent, false)
            ChatViewHolder.Left(binding)
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder.Left -> holder.bind(getItem(position))
            is ChatViewHolder.Right -> holder.bind(getItem(position))
        }
    }

    sealed class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class Left(private val binding: ItemChatLeftBinding) : ChatViewHolder(binding.root) {
            fun bind(msg: ChatMessage) {
                binding.messageText.text = msg.content
                binding.userText.text = msg.senderId
            }
        }

        class Right(private val binding: ItemChatRightBinding) : ChatViewHolder(binding.root) {
            fun bind(msg: ChatMessage) {
                binding.messageText.text = msg.content
                binding.retryIcon.isVisible = !msg.isSent
                // No userText if self
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(old: ChatMessage, new: ChatMessage): Boolean =
            old.timestamp == new.timestamp

        override fun areContentsTheSame(old: ChatMessage, new: ChatMessage): Boolean =
            old == new
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == userId) 1 else 0
    }
}