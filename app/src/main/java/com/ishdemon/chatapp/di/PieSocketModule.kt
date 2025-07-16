package com.ishdemon.chatapp.di

import com.ishdemon.chatapp.BuildConfig
import com.ishdemon.chatapp.network.SocketManager
import com.piesocket.channels.misc.PieSocketOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PieSocketModule {

    @Provides
    @Singleton
    fun providePieSocketOptions(): PieSocketOptions {
        return PieSocketOptions().apply {
            clusterId = BuildConfig.PIESOCKET_CLUSTER_ID
            apiKey = BuildConfig.PIESOCKET_API_KEY
            setNotifySelf(false)
        }
    }

    @Provides
    @Singleton
    fun provideSocketManager(
        options: PieSocketOptions
    ): SocketManager {
        return SocketManager(options)
    }
}
