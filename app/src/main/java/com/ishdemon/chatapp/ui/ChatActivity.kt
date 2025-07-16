package com.ishdemon.chatapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishdemon.chatapp.adapter.ChatAdapter
import com.ishdemon.chatapp.viewmodel.ChatViewModel
import com.ishdemon.chatapp.data.ChatMessage
import com.ishdemon.chatapp.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getStringExtra("userId") ?: return
        val roomId = intent.getStringExtra("roomId") ?: return

        viewModel.connect(roomId,userId)

        val adapter = ChatAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val msg = binding.messageInput.text.toString()
            val chatMsg = ChatMessage(roomId, userId, msg, System.currentTimeMillis())
            viewModel.sendMessage(roomId,chatMsg)
            binding.messageInput.text.clear()
        }

        viewModel.messages.observe(this) {
            adapter.submitList(it)
        }
    }

    companion object {
        fun launch(context: Context, room: String, userId: String) {
            context.startActivity(Intent(context, ChatActivity::class.java).also {
                it.putExtra("roomId", room)
                it.putExtra("userId", userId)
            })
        }
    }
}