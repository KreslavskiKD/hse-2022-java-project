package ru.hse.fmcs.tickgame.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.hse.fmcs.tickgame.LobbyActivity
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class MainMenuFragment : Fragment() {
    private val viewModel : StartActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_menu, container, false)
        val quitBtn : Button = view.findViewById(R.id.main_menu_quit_btn)
        quitBtn.setOnClickListener {
            activity?.finish()
        }

        val joinPrivate : Button = view.findViewById(R.id.join_private)
        joinPrivate.setOnClickListener {
        val intent = Intent(activity, LobbyActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}