package com.synthesizer7.androiddatabinding.view.chatroom

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.synthesizer7.androiddatabinding.api.local.UserDataManager
import com.synthesizer7.androiddatabinding.extenstions.addTo
import com.synthesizer7.androiddatabinding.model.Message
import com.synthesizer7.androiddatabinding.usecase.ChatUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ChatRoomViewModel(val chatUseCase: ChatUseCase,
                        val userDataManager: UserDataManager) : ViewModel() {

    val toastMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val isCanSendMessage = MutableLiveData<Boolean>()
    val isContentReady = MutableLiveData<Boolean>()
    val messageList = MutableLiveData<MutableList<Message>>()

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getMessages() {
        chatUseCase.getMessages(userId = userDataManager.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    isLoading.value = true
                    isContentReady.value = false
                }
                .doFinally {
                    isLoading.value = false
                    isContentReady.value = true
                }
                .doOnSuccess {
                    messageList.value = it.toMutableList()
                }
                .doOnError {
                }
                .subscribe()
                .addTo(composite = compositeDisposable)
    }

    fun subscriptMessageNode() {
        chatUseCase.subscriptMessage(userId = userDataManager.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    val messages = messageList.value
                    messages?.add(it)
                    messageList.value = messages
                }
                .subscribe()
                .addTo(composite = compositeDisposable)
    }

    fun sendMessage(message: String) {
        chatUseCase.sendMessage(senderId = userDataManager.getUserId(),
                name = userDataManager.getUserName(),
                message = message)
                .subscribeOn(Schedulers.io())
                .doOnSuccess {
                    toastMessage.value = "Sent message success"
                }
                .doOnError {
                    toastMessage.value = "Sent message failed"
                }
                .subscribe()
                .addTo(composite = compositeDisposable)
    }

    fun trypingMessage(count: Int) {
        isCanSendMessage.value = count > 0
    }
}