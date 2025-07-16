package com.ishdemon.chatapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ishdemon.chatapp.data.MessageDao
import com.ishdemon.chatapp.data.PendingMessage

@Database(entities = [PendingMessage::class], version = 1)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}
