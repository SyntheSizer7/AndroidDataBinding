package com.synthesizer7.androiddatabinding.view.profile

import android.arch.lifecycle.ViewModel
import com.synthesizer7.androiddatabinding.api.local.UserDataManager

class ProfileViewModel(private val userDataManager: UserDataManager) : ViewModel() {
    fun setUserId(id: String) {
        userDataManager.setUserId(id = id)
    }

    fun setUserName(name: String) {
        userDataManager.setUserName(name = name)
    }
}