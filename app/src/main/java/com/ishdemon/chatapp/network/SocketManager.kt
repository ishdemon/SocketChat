package com.ishdemon.chatapp.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishdemon.chatapp.data.ChatMessage
import com.ishdemon.chatapp.data.toJson
import com.piesocket.channels.Channel
import com.piesocket.channels.PieSocket
import com.piesocket.channels.misc.PieSocketEvent
import com.piesocket.channels.misc.PieSocketEventListener
import com.piesocket.channels.misc.PieSocketOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val options: PieSocketOptions
) {
    private val pieSocket = PieSocket(options)
    private val channels = mutableMapOf<String, Channel>()

    private val connectionStatusMap = mutableMapOf<String, MutableLiveData<Boolean>>()

    fun isConnected(roomId: String): LiveData<Boolean> {
        return connectionStatusMap.getOrPut(roomId) { MutableLiveData(false) }
    }

    fun connect(roomId: String, onMessageReceived: (String) -> Unit) {
        channels[roomId]?.disconnect()
        channels[roomId]?.connect(roomId)
        val channel = pieSocket.join(roomId)
        channel.listen("system:connected", object : PieSocketEventListener() {
            override fun handleEvent(event: PieSocketEvent?) {
                Log.d("PieSocket", "Connected to $roomId")
                connectionStatusMap.getOrPut(roomId) { MutableLiveData() }.postValue(true)
            }
        })

        channel.listen("system:error", object : PieSocketEventListener() {
            override fun handleEvent(event: PieSocketEvent?) {
                //Log.d("PieSocket", "Connected to $roomId")
                disconnect(roomId)
                connectionStatusMap.getOrPut(roomId) { MutableLiveData() }.postValue(false)
            }
        })

        channel.listen("new-message", object : PieSocketEventListener() {
            override fun handleEvent(event: PieSocketEvent?) {
                val raw = event?.data ?: return
                try {
                    onMessageReceived(raw)
                } catch (e: Exception) {
                    Log.e("PieSocket", "Failed to parse message: $raw")
                }
            }
        })

        channels[roomId] = channel
    }

    fun sendMessage(roomId: String, message: ChatMessage) {
        val event = PieSocketEvent("new-message")
        event.setData(message.toJson())
        channels[roomId]?.publish(event)
    }

    fun disconnect(roomId: String) {
        pieSocket.leave(roomId)
        channels[roomId]?.disconnect()
        channels.remove(roomId)
        connectionStatusMap[roomId]?.postValue(false)
    }

    fun disconnectAll() {
        channels.values.forEach { it.disconnect() }
        channels.clear()
    }
}