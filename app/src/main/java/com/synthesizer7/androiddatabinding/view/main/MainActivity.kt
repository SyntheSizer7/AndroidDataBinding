package com.synthesizer7.androiddatabinding.view.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.synthesizer7.androiddatabinding.R

import com.synthesizer7.androiddatabinding.api.local.UserDataManagerImpl
import com.synthesizer7.androiddatabinding.view.movie.MovieFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserDataManagerImpl.instance.initialize(context = this)

        if (savedInstanceState == null) {
//            if (UserDataManagerImpl.instance.getUserId().isEmpty()) {
//                val profileFragment = ProfileFragment()
//                supportFragmentManager.beginTransaction()
//                    .add(R.id.fragment_container, profileFragment, ChatRoomFragment.TAG)
//                    .addToBackStack("home").commit()
//            } else {
//                val chatRoomFragment = ChatRoomFragment()
//                supportFragmentManager.beginTransaction()
//                    .add(R.id.fragment_container, chatRoomFragment, ChatRoomFragment.TAG).commit()
//            }
        }

        val movieFragment = MovieFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, movieFragment, MovieFragment.TAG).commit()
    }
}
