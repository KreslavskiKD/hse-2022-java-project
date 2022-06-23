package ru.hse.fmcs.tickgame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.hse.fmcs.tickgame.R
import ru.hse.fmcs.tickgame.viewmodels.StartActivityViewModel

class IPFragment : Fragment() {
    private lateinit var userNameTextView: TextView
    private val viewModel : StartActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frame_user_stats, container, false)
        userNameTextView = view.findViewById(R.id.nickname_field)
        userNameTextView.text = ""
        val infotextview : TextView = view.findViewById(R.id.other)
        infotextview.text = ""

        val iptextview : TextView = view.findViewById(R.id.ip_field)
        viewModel.currentIP.observe(viewLifecycleOwner) {
            iptextview.text = it
        }

        return view
    }
}