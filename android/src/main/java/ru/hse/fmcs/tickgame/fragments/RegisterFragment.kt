package ru.hse.fmcs.tickgame.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.data.User
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class RegisterFragment : Fragment() {
    private val viewModel : StartActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_register, container, false)

        val registerBtn : Button = view.findViewById(R.id.register_btn)
        val loginTextField : EditText = view.findViewById(R.id.reg_login_edit_text)
        val passwordTextField : EditText = view.findViewById(R.id.reg_password_edit_text)
        val repeatPasswordTextField : EditText = view.findViewById(R.id.reg_password_edit_text_repeat)
        val info : TextView = view.findViewById(R.id.txtInfo)

        registerBtn.setOnClickListener {
            info.text = ""
            Log.d(TAG, "In setOnClickListener")
            val login = loginTextField.text.toString()
            val password = passwordTextField.text.toString()
            val repPassword = repeatPasswordTextField.text.toString()
            if (!(login == "" || password == "") && repPassword == password) {
                val message = viewModel.register(User(login, password))
                if (message != "ok") {
                    val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
                    toast.show()
                }
            } else if (repPassword != password) {
                info.text = "Passwords don't match"
            }
        }

        return view
    }

    companion object {
        const val TAG = "RegisterFragment"
    }
}