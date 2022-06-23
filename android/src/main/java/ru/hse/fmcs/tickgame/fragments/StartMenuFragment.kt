package ru.hse.fmcs.tickgame.fragments

import android.app.AlertDialog
import android.net.InetAddresses
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.hse.fmcs.tickgame.GameContext
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel


class StartMenuFragment : Fragment() {
    private val viewModel : StartActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_menu_start, container, false)

        val loginBtn : Button = view.findViewById(R.id.goto_login_btn)
        val registerBtn : Button = view.findViewById(R.id.goto_register_btn)
        val connectBtn : Button = view.findViewById(R.id.connect_btn)
        val quitBtn : Button = view.findViewById(R.id.quit_btn)
        quitBtn.setOnClickListener {
            activity?.finish()
        }

        loginBtn.setOnClickListener {
            viewModel.setUiState(UIState.Login())
        }

        registerBtn.setOnClickListener {
            viewModel.setUiState(UIState.Register())
        }

        connectBtn.setOnClickListener {
            val alert: AlertDialog.Builder = AlertDialog.Builder(context)
            var address : String
            val edittext = EditText(context)
            alert.setMessage("")
            alert.setTitle("Enter server address")

            alert.setView(edittext)

            alert.setPositiveButton("Ok") { dialog, whichButton ->
                address = edittext.text.toString()
                if (isIpValid(address)) {
                    GameContext.setServerAddress(address)
                    viewModel.currentIP.value = address
                } else {
                    val toast = Toast.makeText(activity, "Not an IP", Toast.LENGTH_LONG)
                    toast.show()
                }
            }

            alert.setNegativeButton("Cancel") { dialog, whichButton ->
                // what ever you want to do with No option.
            }

            alert.show()

        }

        return view
    }

    fun isIpValid(ip: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            InetAddresses.isNumericAddress(ip)
        } else {
            Patterns.IP_ADDRESS.matcher(ip).matches()
        }
    }

}