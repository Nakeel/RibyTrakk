package com.we2dx.ribytrakks.ui.activity.ui.trakklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.we2dx.ribytrakks.R

class TrakkListFragment : Fragment() {

    private lateinit var trakkListViewModel: TrakkListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trakkListViewModel =
            ViewModelProviders.of(this).get(TrakkListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_trakk_list, container, false)
        trakkListViewModel.text.observe(this, Observer {
//            textView.text = it
        })
        return root
    }
}