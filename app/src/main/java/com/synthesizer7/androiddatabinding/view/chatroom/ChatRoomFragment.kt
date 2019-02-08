package com.synthesizer7.androiddatabinding.view.chatroom

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.synthesizer7.androiddatabinding.R
import com.synthesizer7.androiddatabinding.api.local.UserDataManagerImpl
import com.synthesizer7.androiddatabinding.api.repo.ChatRepoImpl
import com.synthesizer7.androiddatabinding.databinding.FragmentChatRoomBinding
import com.synthesizer7.androiddatabinding.extenstions.getViewModel
import com.synthesizer7.androiddatabinding.usecase.ChatUseCaseImpl
import com.synthesizer7.androiddatabinding.view.chatroom.adapter.ChatRoomMessageAdapter
import kotlinx.android.synthetic.main.fragment_chat_room.*

class ChatRoomFragment : Fragment() {

    companion object {
        const val TAG = "ChatRoomFragment"
    }

    private val viewModel: ChatRoomViewModel by lazy {
        getViewModel {
            ChatRoomViewModel(
                    chatUseCase = ChatUseCaseImpl(
                            chatRepo = ChatRepoImpl(firebaseDatabase = FirebaseDatabase.getInstance())
                    ),
                    userDataManager = UserDataManagerImpl.instance
            )
        }
    }

    private val chatMessageAdapter: ChatRoomMessageAdapter by lazy {
        ChatRoomMessageAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = DataBindingUtil.inflate<FragmentChatRoomBinding>(inflater,
                R.layout.fragment_chat_room, container, false).apply {
            model = viewModel
        }
        view.setLifecycleOwner(this)

        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingViewModel()
        initView()

        viewModel.getMessages()
        viewModel.subscriptMessageNode()
    }

    private fun initView() {
        messageRecycleView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = chatMessageAdapter
        }

        messageEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                messageRecycleView.scrollToPosition(chatMessageAdapter.itemCount - 1)
            }
        }
    }

    private fun bindingViewModel() {
        viewModel.messageList.observe(this, Observer { messageList ->
            messageList?.let {
                chatMessageAdapter.setListData(list = it)
                chatMessageAdapter.notifyDataSetChanged()
                messageRecycleView.scrollToPosition(chatMessageAdapter.itemCount - 1)
            }
        })

        viewModel.toastMessage.observe(this, Observer { toastMessage ->
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        })
    }
}