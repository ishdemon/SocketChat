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
    private val _connected = MutableLiveData<Boolean>()
    val connected: LiveData<Boolean> get() = _connected

    fun connect(roomId: String, onMessageReceived: (String) -> Unit) {
        if (channels.containsKey(roomId)) return

        val channel = pieSocket.join(roomId)

        channel.listen("system:connected", object : PieSocketEventListener() {
            override fun handleEvent(event: PieSocketEvent?) {
                Log.d("PieSocket", "Connected to $roomId")
                _connected.postValue(true)
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

    fun isConnected(roomId: String): Boolean {
        return channels[roomId] != null && connected.value == true
    }

    fun sendMessage(roomId: String, message: ChatMessage) {
        val event = PieSocketEvent("new-message")
        event.setData(message.toJson())
        channels[roomId]?.publish(event)
    }

    fun disconnect(roomId: String) {
        channels[roomId]?.disconnect()
        channels.remove(roomId)
    }

    fun disconnectAll() {
        channels.values.forEach { it.disconnect() }
        channels.clear()
    }
}