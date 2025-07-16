package com.ishdemon.chatapp.data

data class ChatRoom(
    val roomId: String,
    val title: String,
    val lastMessage: String,
    val lastTimestamp: Long,
    val unreadCount: Int = 0
)