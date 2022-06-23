package ru.hse.fmcs.tickgame.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class LoginFragment : Fragment() {
    private val viewModel : StartActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_login, container, false)
        Log.d(TAG, "LoginFragment inflated")
        val loginBtn : Button = view.findViewById(R.id.login_btn)
        val registerBtn : Button = view.findViewById(R.id.goto_reg_from_login)

        val loginTextField : EditText = view.findViewById(R.id.login_edit_text)
        val passwordTextField : EditText = view.findViewById(R.id.password_edit_text)

        loginBtn.setOnClickListener {
            val login = loginTextField.text.toString()
            val password = passwordTextField.text.toString()
            if (!(login == "" || password == "")) {
                viewModel.viewModelScope.launch {
                    val message = viewModel.login(User(login, password))
                    if (message != "ok") {
                        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
                        toast.show()
                    }
                }

            }
        }

        registerBtn.setOnClickListener {
            viewModel.setUiState(UIState.Register())
        }

        val quitBtn : Button = view.findViewById(R.id.quit_login_btn)
        quitBtn.setOnClickListener {
            viewModel.setUiState(UIState.Start())
        }


        return view
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}