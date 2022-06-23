package ru.hse.fmcs.tickgame.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.fmcs.tickgame.GameContext
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.data.noUser
import ru.hse.fmcs.tickgame.models.AuthState
import ru.hse.fmcs.tickgame.utils.UserAuthStateFlow
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.utils.Constants

class StartActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val shp: SharedPreferences = application.getSharedPreferences(
        Constants.PREFERENCES_KEY,
        Context.MODE_PRIVATE
    )
    private val shpEditor: SharedPreferences.Editor = shp.edit()
    private val _uiState = MutableStateFlow<UIState>(UIState.Login())
    val uiState = _uiState.asStateFlow()
    private val uasf = UserAuthStateFlow(getApplication())
    private val authStateFlow: StateFlow<AuthState> = uasf.stateFlow
    val currentUser = MutableLiveData<User>()
    val currentIP = MutableLiveData<String>()

    private var curUser : User = noUser

    init {
        viewModelScope.launch {
            currentIP.value = getCached()
            // uasf.tryLogin()
            authStateFlow.collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        Log.d(TAG, "Authenticated")
                        _uiState.emit(UIState.Menu())
                        GameContext.setLogin(authState.authenticatedUser.login)
                        setUserData(authState.authenticatedUser)
                    }
                    is AuthState.InProcess -> {
                        Log.d(TAG, "InProcess")
                        curUser = authState.cachedUser
                    }
                    is AuthState.Unauthenticated -> {
                        Log.d(TAG, "Unauthenticated")

                        _uiState.emit(UIState.Start())
                        Log.d(TAG, "Unauthenticated after emit")
                    }
                }
            }
        }

    }

    suspend fun login(user: User) : String {
        var message = "ok"
        try {
            Log.d(TAG, "login")
            setCache(currentIP.value!!)
            val res = uasf.login(user)
            if (!res) {
                message = uasf.getMessage()
            }
        } catch (e : Exception) {
            message = e.message.toString()
        }
        return message
    }

    suspend fun register(user: User) : String {
        var message = "ok"

        try {
            Log.d(TAG, "register")
            setCache(currentIP.value!!)
            val res = uasf.register(user)
            if (!res) {
                message = uasf.getMessage()
            }
        } catch (e : Exception) {
            message = e.message.toString()
        }

        return message
    }

    fun setUserData(user: User) {
        //// TODO CRITICAL remove this log after release
        //// Log.d(TAG, "setUserData: login - ${user.login}, password - ${user.password}")
        //// TODO ---------------------------------------
        currentUser.value = user
    }

    fun setUiState(state: UIState) = viewModelScope.launch {
            _uiState.emit(state)
        }


    private suspend fun hasCached(): Boolean = withContext(Dispatchers.IO) {
        Log.d(TAG, "hasCached")
        val ip = shp.getString(IP, "")
        return@withContext (ip != null && ip != "")
    }

    private suspend fun getCached(): String = withContext(Dispatchers.IO) {
        Log.d(TAG, "getCached")
        if (!hasCached()) return@withContext GameContext.getServerAddress()
        val ip = shp.getString(IP, "")
        return@withContext ip!!
    }

    private suspend fun setCache(ip : String) = withContext(Dispatchers.IO) {
        Log.d(TAG, "setCache")
        shpEditor.putString(IP, ip)
        shpEditor.commit()
    }

    companion object {
        const val TAG = "StartActivityViewModel"
        const val IP = "ip"
    }
}
