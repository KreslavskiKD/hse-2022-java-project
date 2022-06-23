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
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.activities.RoomActivity
import ru.hse.fmcs.tickgame.models.UIState
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class ChooseLobbyFragment : Fragment() {

    private val viewModel : StartActivityViewModel by viewModels();

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
            val intent = Intent(activity, RoomActivity::class.java)
            intent.putExtra("lobby_id", lobbyId)
//            viewModel.setUiState(UIState.Menu())
            startActivity(intent)

        }

        return view
    }
}