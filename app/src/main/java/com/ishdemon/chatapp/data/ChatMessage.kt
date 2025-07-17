package com.ishdemon.chatapp.data

import com.google.gson.Gson
import com.ishdemon.chatapp.data.PendingMessage
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val roomId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    var isSent: Boolean = true
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