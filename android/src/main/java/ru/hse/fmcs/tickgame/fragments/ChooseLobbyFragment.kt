package ru.hse.fmcs.tickgame.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.hse.fmcs.tickgame.LobbyActivity
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.viewmodels.LobbyActivityViewModel

class ChooseLobbyFragment : Fragment() {

    val viewModel : LobbyActivityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_get_lobby, container, false)

        val getInBtn : Button = view.findViewById(R.id.get_in_btn)
        val lobbyIdTextView : EditText = view.findViewById(R.id.lobby_id_text)

        getInBtn.setOnClickListener {
            val lobbyId = lobbyIdTextView.text.toString()
            // connect to lobby somehow
            // or maybe lobbyId should be transfered to LobbyActivity and used there to connect
            // if successful go to Lobby activity
            val intent = Intent(activity, LobbyActivity::class.java)
            // TODO some additional information should be added to intent later
            startActivity(intent)

        }

        return view
    }
}