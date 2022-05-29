package ru.hse.fmcs.tickgame.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchUnscoped
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.data.noUser
import ru.hse.fmcs.tickgame.models.AuthState
import ru.hse.fmcs.tickgame.utils.UserAuthStateFlow
import ru.hse.fmcs.tickgame.models.UIState

class StartActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<UIState>(UIState.Login())
    val uiState = _uiState.asStateFlow()
    private val uasf = UserAuthStateFlow(getApplication())
    private val authStateFlow: StateFlow<AuthState> = uasf.stateFlow
    val currentUser = MutableLiveData<User>()
//    val pressedBtns = MutableLiveData<User>()

    private var curUser : User = noUser

    init {
        viewModelScope.launch {
            uasf.tryLogin()
            authStateFlow.collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        Log.d(TAG, "Authenticated")
                        _uiState.emit(UIState.Menu())
                        setUserData(authState.authenticatedUser)
                    }
                    is AuthState.InProcess -> {
                        Log.d(TAG, "InProcess")
                        curUser = authState.cachedUser
                    }
                    is AuthState.Unauthenticated -> {
                        Log.d(TAG, "Unauthenticated")

                        _uiState.emit(UIState.Login())
                        Log.d(TAG, "Unauthenticated after emit")
//                        waitForLogin()
                    }
                }
            }
        }

    }

    suspend fun login(user: User) {
        Log.d(TAG, "login")
        currentCoroutineContext().launchUnscoped {
            uasf.login(user)
        }
    }

    fun setUserData(user: User) {
        //// TODO CRITICAL remove this log after release
        Log.d(TAG, "setUserData: login - ${user.login}, password - ${user.password}")
        //// TODO ---------------------------------------
        currentUser.value = user
    }

    companion object {
        const val TAG = "StartActivityViewModel"
    }

}