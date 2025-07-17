package com.ishdemon.chatapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_messages")
data class PendingMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val senderId: String,
    val roomId: String,
    val content: String,
    val timestamp: Long
){

    fun ToChatMessage() = ChatMessage(
        roomId = roomId,
        senderId = senderId,
        content = content,
        timestamp = timestamp,
        isSent = false
    )
}