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
import dagger.hilt.android.lifecycle.HiltViewModel
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
        socketManager.sendMessage(roomId,message)
        messageList.add(message)
        _messages.postValue(messageList.toList())
    }

    fun disconnect(roomId: String) {
        socketManager.disconnect(roomId)
    }

    private fun queueOffline(msg: ChatMessage) {
        viewModelScope.launch {
            messageDao.insert(
                msg.ToEntity()
            )
        }
    }

    private suspend fun retryQueuedMessages(roomId: String) {
        val pending = messageDao.getAll()
        for (p in pending) {
            val msg = p.ToChatMessage()
            if (socketManager.isConnected(roomId)) {
                socketManager.sendMessage(roomId,msg)
                messageDao.delete(p)
            }
        }
    }
}