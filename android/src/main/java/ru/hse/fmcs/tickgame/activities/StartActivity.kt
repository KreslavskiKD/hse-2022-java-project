package ru.hse.fmcs.tickgame.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.fragments.*
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        try {
            supportActionBar!!.hide()
        } catch (e : NullPointerException) {}

        setContentView(R.layout.activity_start)
        val viewModel : StartActivityViewModel by viewModels()

        Log.d(TAG, "Before launch")
        lifecycleScope.launchWhenStarted {
            Log.d(TAG, "In launch")
            viewModel.uiState.collect {
                Log.d(TAG, "In collect")
                when (it) {
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
                        supportFragmentManager.beginTransaction().replace(R.id.right_column_fl, IPFragment()).commit()
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

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}
