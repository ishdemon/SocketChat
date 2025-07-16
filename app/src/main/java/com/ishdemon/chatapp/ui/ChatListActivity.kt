package com.ishdemon.chatapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val userId = intent.getStringExtra("userId") ?: return

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = ChatListAdapter { room ->
            ChatActivity.launch(this, room.roomId,userId)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.chatRooms.observe(this) {
            adapter.submitList(it)
        }
    }

    companion object {
        fun launch(context: Context,userId: String) {
            context.startActivity(Intent(context, ChatListActivity::class.java).also {
                it.putExtra("userId",userId)
            })
        }
    }
}