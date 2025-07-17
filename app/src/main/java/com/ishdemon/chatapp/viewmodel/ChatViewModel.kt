package com.ishdemon.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishdemon.chatapp.network.SocketManager
import com.ishdemon.chatapp.data.ChatMessage
import com.ishdemon.chatapp.data.MessageDao
import com.ishdemon.chatapp.data.ToEntity
import com.ishdemon.chatapp.data.toChatMessage
import com.ishdemon.chatapp.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val messageDao: MessageDao

) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    private val messageList = mutableListOf<ChatMessage>()

    fun isConnected(roomId: String): LiveData<Boolean> = socketManager.isConnected(roomId)

    fun connect(roomId: String, userId: String) {
        socketManager.connect(roomId) { json ->
            val message = json.toChatMessage()
            messageList.add(message)
            _messages.postValue(messageList.toList())
//            if (message.senderId != userId) {
//                messageList.add(message)
//                _messages.postValue(messageList.toList())
//            }
        }
    }

    fun sendMessage(roomId: String,message: ChatMessage) {
        if(isConnected(roomId).value == true) {
            socketManager.sendMessage(roomId, message)
        } else queueOffline(message)
        messageList.add(message)
        _messages.postValue(messageList.toList())
    }

    fun disconnect(roomId: String) {
        socketManager.disconnect(roomId)
    }

    fun clearDb() {
        viewModelScope.launch {
            messageDao.deleteAll()
        }
    }

    private fun queueOffline(msg: ChatMessage) {
        viewModelScope.launch {
            messageDao.insert(
                msg.ToEntity()
            )
        }
    }

    suspend fun retryQueuedMessages() {
        val pending = messageDao.getAll()
        for (p in pending) {
            val msg = p.ToChatMessage()
            if (isConnected(msg.roomId).value == true) {
                socketManager.sendMessage(msg.roomId,msg)
                messageDao.delete(p)
            }
        }
    }
}