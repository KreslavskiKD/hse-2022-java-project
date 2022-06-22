package ru.hse.fmcs.tickgame.activities

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.viewmodels.LobbyActivityViewModel


class LobbyActivity : AppCompatActivity() {

    private val viewModel: LobbyActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_Dark)
        } else {
            setTheme(R.style.Theme_Light)
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            supportActionBar!!.hide()
        } catch (e: NullPointerException) {}


        setContentView(R.layout.activity_lobby)

    }

}