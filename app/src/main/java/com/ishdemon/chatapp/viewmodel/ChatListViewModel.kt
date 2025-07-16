package com.ishdemon.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishdemon.chatapp.data.ChatRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {

    private val _chatRooms = MutableLiveData<List<ChatRoom>>()
    val chatRooms: LiveData<List<ChatRoom>> = _chatRooms

    init {
        _chatRooms.value = listOf(
            ChatRoom("chatroom1", "General", "Welcome", System.currentTimeMillis()),
            ChatRoom("chatroom2", "Random", "Hello", System.currentTimeMillis() - 2000)
        )
    }
}