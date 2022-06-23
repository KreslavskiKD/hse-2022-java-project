package ru.hse.fmcs.tickgame.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.fragments.*
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            supportActionBar!!.hide()
        } catch (e : NullPointerException) {}

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_Dark)
        } else {
            setTheme(R.style.Theme_Light)
        }
        setContentView(R.layout.activity_start)
        val viewModel : StartActivityViewModel by viewModels()

        Log.d(TAG, "Before launch")
        lifecycleScope.launchWhenStarted {
            Log.d(TAG, "In launch")
            viewModel.uiState.collect {
                Log.d(TAG, "In collect")
                when (it) {
//                    is UIState.ChooseLobby -> {
//                        Log.d(TAG, "ChooseLobby")
//
//                        supportFragmentManager.beginTransaction().replace(R.id.left_column_fl, EmptyFragment()).commit()
//                        supportFragmentManager.beginTransaction().replace(R.id.center_column_fl, ChooseLobbyFragment()).commit()
//                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, EmptyFragment()).commit()
//                    }
                    is UIState.Quit -> {
                        Log.d(TAG, "Quit")
                        onDestroy()
                    }
                    is UIState.Login -> {
                        Log.d(TAG, "Login")

                        supportFragmentManager.beginTransaction().replace(R.id.left_column_fl, EmptyFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.center_column_fl, LoginFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, EmptyFragment()).commit()
                    }
                    is UIState.Start -> {
                        Log.d(TAG, "Start")

                        supportFragmentManager.beginTransaction().replace(R.id.left_column_fl, EmptyFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.center_column_fl, StartMenuFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, EmptyFragment()).commit()
                    }
                    is UIState.Register -> {
                        Log.d(TAG, "Register")
                        supportFragmentManager.beginTransaction().replace(R.id.left_column_fl, EmptyFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.center_column_fl, RegisterFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, EmptyFragment()).commit()
                    }
                    is UIState.Menu -> {
                        Log.d(TAG, "Menu")

  //                      supportFragmentManager.beginTransaction().replace(R.id.left_column_fl, SettingsFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.center_column_fl, MainMenuFragment()).commit()
                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, UserStatsFragment()).commit()
                    }
                }
                Log.d(TAG, "After when")
            }
        }


    }

    companion object {
        const val TAG = "StartActivity:"
    }

}
