package com.ishdemon.chatapp.data

import com.google.gson.Gson
import com.ishdemon.chatapp.data.PendingMessage

data class ChatMessage(
    val roomId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)

fun ChatMessage.ToEntity() = PendingMessage(
    senderId = senderId,
    roomId = roomId,
    content = content,
    timestamp = timestamp
)

fun ChatMessage.toJson(): String {
    return Gson().toJson(this)
}

fun String.toChatMessage(): ChatMessage {
    return Gson().fromJson(this, ChatMessage::class.java)
}