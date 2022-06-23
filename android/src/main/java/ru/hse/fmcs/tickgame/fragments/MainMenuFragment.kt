package ru.hse.fmcs.tickgame.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.activities.RoomActivity
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class MainMenuFragment : Fragment() {

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

            val alert: AlertDialog.Builder = AlertDialog.Builder(context)
            var lobbyId : String
            val edittext = EditText(context)
            alert.setMessage("")
            alert.setTitle("Enter room id")

            alert.setView(edittext)

            alert.setPositiveButton("Ok") { dialog, whichButton ->
                lobbyId = edittext.text.toString()
                val intent = Intent(activity, RoomActivity::class.java)
                intent.putExtra("lobby_id", lobbyId)
                startActivity(intent)
            }

            alert.setNegativeButton("Cancel") { dialog, whichButton ->
                // what ever you want to do with No option.
            }

            alert.show()

        }

        return view
    }
}