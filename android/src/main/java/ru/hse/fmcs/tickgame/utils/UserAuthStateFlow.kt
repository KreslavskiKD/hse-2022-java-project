package ru.hse.fmcs.tickgame.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.hse.fmcs.Account
import ru.hse.fmcs.AccountServiceGrpc
import ru.hse.fmcs.tickgame.GameContext
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.data.noUser
import ru.hse.fmcs.tickgame.models.AuthState


class UserAuthStateFlow(var context: Context)  {

    private val shp: SharedPreferences = context.getSharedPreferences(Constants.PREFERENCES_KEY, MODE_PRIVATE)
    private val shpEditor: SharedPreferences.Editor = shp.edit()
    private val _stateFlow: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Unauthenticated())
    val stateFlow: StateFlow<AuthState> get() = _stateFlow

    private var channel = ManagedChannelBuilder
        .forAddress(GameContext.getServerAddress(), GameContext.getLoginPort())
        .usePlaintext()
        .build()

    private var stub = AccountServiceGrpc.newBlockingStub(channel)

    private var message : String = ""

    // not used now due to concept changes
    suspend fun tryLogin() {
        if (hasCached()) {
            val currentUser = getCached()
            _stateFlow.value = AuthState.InProcess(currentUser)
            if (login(currentUser)) {
                _stateFlow.value = AuthState.Authenticated(currentUser)
                Log.d(TAG, "tryLogin stateFlow value set")
                // if it was successful we have to cache it
                GlobalScope.launch(Dispatchers.IO) {
                    setCache(currentUser)
                }
            }
        }
    }

    suspend fun login(user: User) : Boolean {
        Log.d(TAG, "login")

        channel = ManagedChannelBuilder
            .forAddress(GameContext.getServerAddress(), GameContext.getLoginPort())
            .usePlaintext()
            .build()

        stub = AccountServiceGrpc.newBlockingStub(channel)

        val lres: Account.LoginResponse = stub.login(
            Account.LoginRequest.newBuilder().setLogin(user.login).setPassword(user.password).build()
        )
        message = lres.comment
        if (lres.success) {
            _stateFlow.value = AuthState.Authenticated(user)
            Log.d(TAG, "login stateFlow value set")
            // if it was successful we have to cache it
            GlobalScope.launch(Dispatchers.IO) {
                setCache(user)
            }
            return true
        }

        return false
    }

    fun getMessage() : String {
        return message
    }

    suspend fun register(user: User) : Boolean {
        Log.d(TAG, "register")

        channel = ManagedChannelBuilder
            .forAddress(GameContext.getServerAddress(), GameContext.getLoginPort())
            .usePlaintext()
            .build()

        stub = AccountServiceGrpc.newBlockingStub(channel)

        val regRes = stub.registerAccount(
            Account.RegisterAccountRequest.newBuilder().setLogin(user.login).setPassword(user.password).build()
        )
        message = regRes.comment
        if (regRes.success) {
            _stateFlow.value = AuthState.Authenticated(user)
            Log.d(TAG, "register stateFlow value set")
            // if it was successful we have to cache it
            GlobalScope.launch(Dispatchers.IO) {
                setCache(user)
            }
            return true
        }
        return false
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