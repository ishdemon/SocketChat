package com.ishdemon.chatapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM pending_messages")
    suspend fun getAll(): List<PendingMessage>

    @Insert
    suspend fun insert(message: PendingMessage)

    @Delete
    suspend fun delete(message: PendingMessage)

    @Query("DELETE FROM pending_messages")
    suspend fun deleteAll()
}