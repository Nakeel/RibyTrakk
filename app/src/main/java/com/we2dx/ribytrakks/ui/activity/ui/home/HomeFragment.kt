package com.we2dx.ribytrakks.ui.activity.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.we2dx.ribytrakks.R
import com.we2dx.ribytrakks.ui.activity.TrakkDetails

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var trakkOnGoingAnim: LottieAnimationView
    private lateinit var mTrakkState : AppCompatButton
    private lateinit var mLastTrakkLayout : LinearLayout
    private lateinit var mLastTrakkId : AppCompatTextView
    private lateinit var mViewLastTrakkButton : AppCompatButton
    private var mTrakkStarted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        trakkOnGoingAnim = root.findViewById(R.id.trakk_anim)

        mTrakkState = root.findViewById(R.id.trakk_state_text)
        mLastTrakkId = root.findViewById(R.id.last_trakk_id_text)
        mLastTrakkLayout = root.findViewById(R.id.last_trakk_layout)
        mViewLastTrakkButton =  root.findViewById(R.id.view_trakk_details)


        homeViewModel.text.observe(this, Observer {
//            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trakkOnGoingAnim.setOnClickListener {
            startTrakking()
        }
        mTrakkState.setOnClickListener { startTrakking() }
        mViewLastTrakkButton.setOnClickListener {
            startViewDetail()
        }
    }

    private fun startViewDetail() {
        startActivity(Intent(context,TrakkDetails::class.java))
    }

    private fun startTrakking() {
        if(!mTrakkStarted){
            if (!trakkOnGoingAnim.isAnimating) {
                trakkOnGoingAnim.playAnimation()
                trakkOnGoingAnim.loop(true)
            }
            mTrakkStarted = true
            mTrakkState.text = "End Trakk"
            mLastTrakkLayout.visibility = View.GONE

        }else {
            if (trakkOnGoingAnim.isAnimating)
            {
                trakkOnGoingAnim.loop(false)
                trakkOnGoingAnim.pauseAnimation()}
            mLastTrakkLayout.visibility = View.VISIBLE
            mTrakkState.text = "Start New Trakk"
            mTrakkStarted = false
        }
    }
}