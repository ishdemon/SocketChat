package com.ishdemon.chatapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishdemon.chatapp.adapter.ChatListAdapter
import com.ishdemon.chatapp.viewmodel.ChatListViewModel
import com.ishdemon.chatapp.databinding.ActivityChatListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
    private val viewModel by viewModels<ChatListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ChatListAdapter { room ->
            ChatActivity.launch(this, room.roomId)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.chatRooms.observe(this) {
            adapter.submitList(it)
        }
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ChatListActivity::class.java))
        }
    }
}