package com.ishdemon.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishdemon.chatapp.data.ChatRoom
import com.ishdemon.chatapp.databinding.ItemChatRoomBinding

class ChatListAdapter(
    private val onItemClick: (ChatRoom) -> Unit
) : ListAdapter<ChatRoom, ChatListAdapter.ChatRoomViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ItemChatRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatRoomViewHolder(private val binding: ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatRoom: ChatRoom) {
            binding.chatTitle.text = chatRoom.title
            binding.chatPreview.text = chatRoom.lastMessage

            if (chatRoom.unreadCount > 0) {
                binding.unreadBadge.isVisible = true
                binding.unreadBadge.text = chatRoom.unreadCount.toString()
            } else {
                binding.unreadBadge.isVisible = false
            }

            binding.root.setOnClickListener {
                onItemClick(chatRoom)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(old: ChatRoom, new: ChatRoom): Boolean =
            old.roomId == new.roomId

        override fun areContentsTheSame(old: ChatRoom, new: ChatRoom): Boolean =
            old == new
    }
}