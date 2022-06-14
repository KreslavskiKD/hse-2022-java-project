package ru.hse.fmcs.tickgame

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

//import com.soywiz.korgw.KorgwActivity
//import main
//
//class MainActivity : KorgwActivity() {
//	override suspend fun activityMain() {
//		main()
//	}
//}

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
			setTheme(R.style.Theme_Dark)
		} else {
			setTheme(R.style.Theme_Light)
		}
		super.onCreate(savedInstanceState, persistentState)
	}
}

