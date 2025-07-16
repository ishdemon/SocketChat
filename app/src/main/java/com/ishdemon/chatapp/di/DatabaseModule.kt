package com.ishdemon.chatapp.di

import android.content.Context
import androidx.room.Room
import com.ishdemon.chatapp.ChatDatabase
import com.ishdemon.chatapp.data.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_db"
        ).build()
    }

    @Provides
    fun provideMessageDao(db: ChatDatabase): MessageDao = db.messageDao()
}
