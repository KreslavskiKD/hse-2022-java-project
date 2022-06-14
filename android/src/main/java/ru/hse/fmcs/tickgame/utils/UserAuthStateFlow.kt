package ru.hse.fmcs.tickgame.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.data.noUser
import ru.hse.fmcs.tickgame.models.AuthState

class UserAuthStateFlow(var context: Context)  {

    private val shp: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCES_KEY, MODE_PRIVATE)
    private val shpEditor: SharedPreferences.Editor = shp.edit()
    private val _stateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Unauthenticated())
    val stateFlow: StateFlow<AuthState> get() = _stateFlow

    suspend fun tryLogin() {
        if (hasCached()) {
            val currentUser = getCached()
            _stateFlow.value = AuthState.InProcess(currentUser)
            // actual login with account
            _stateFlow.value = AuthState.Authenticated(currentUser)
        }
    }

    fun login(user: User) {
        Log.d(TAG, "login")
        // TODO("here will be some logging in later")
        _stateFlow.value = AuthState.Authenticated(user)
        Log.d(TAG, "login stateFlow value set")
        // if it was successful we have to cache it
//        setCache(fakeUser)
    }

    fun register(user: User) {
        Log.d(TAG, "login")
        // TODO("here will be some registering in later")
        _stateFlow.value = AuthState.Authenticated(user)
        Log.d(TAG, "register stateFlow value set")
    }

    private suspend fun hasCached(): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "hasCached")
        val userLogin = shp.getString(LOGIN, "")
        val userPassword = shp.getString(PASSWORD, "")
        return@withContext (userLogin != null && userLogin != "") && (userPassword != null && userPassword != "")
    }

    private suspend fun getCached(): User = withContext(Dispatchers.IO) {
        Log.d(TAG, "getCached")
        if (!hasCached()) return@withContext noUser
        val userLogin = shp.getString(LOGIN, "")
        val userPassword = shp.getString(PASSWORD, "")
        return@withContext User(userLogin!!, userPassword!!)
    }

    private suspend fun setCache(user: User) = withContext(Dispatchers.IO) {
        Log.d(TAG, "setCache")
        shpEditor.putString(LOGIN, user.login)
        shpEditor.putString(PASSWORD, user.password)
        shpEditor.commit()
    }

    companion object {
        const val LOGIN = "login"
        const val PASSWORD = "password"

        const val TAG = "UserAuthStateFlow"
    }
}