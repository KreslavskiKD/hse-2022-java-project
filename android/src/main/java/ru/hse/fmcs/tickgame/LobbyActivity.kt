package ru.hse.fmcs.tickgame

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity


class LobbyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_lobby)

    }

}