package ru.hse.fmcs.tickgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

        return view
    }
}