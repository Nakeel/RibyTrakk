package com.we2dx.ribytrakks.ui.activity.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.*
import com.we2dx.ribytrakks.R
import com.we2dx.ribytrakks.database.AppDatabase
import com.we2dx.ribytrakks.database.TrakkData
import com.we2dx.ribytrakks.ui.activity.MainActivity
import com.we2dx.ribytrakks.ui.activity.TrakkDetails
import com.we2dx.ribytrakks.utils.FusedLocationManager

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var trakkOnGoingAnim: LottieAnimationView
    private lateinit var mTrakkState : AppCompatButton
    private lateinit var mLastTrakkLayout : LinearLayout
    private lateinit var mLastTrakkId : AppCompatTextView
    private lateinit var mViewLastTrakkButton : AppCompatButton
    private var mTrakkStarted = false
    private lateinit var mFusedLocationManager : FusedLocationManager
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mStartlocation : Location
    private lateinit var mEndlocation : Location
    private lateinit var mDb : AppDatabase


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mDb = AppDatabase.getInstance(context)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as MainActivity )
//        mFusedLocationManager = FusedLocationManager(mFusedLocationClient,context!!,(activity as MainActivity ))


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
        val intent = Intent(context,TrakkDetails::class.java)
        intent.putExtra("Start_lat",start_lat)
        intent.putExtra("Start_lng",start_lng)
        intent.putExtra("End_lat",end_lat)
        intent.putExtra("End_lng",end_lng)
        startActivity(intent)
    }

    private var start_lat : Float? = 0f

    private var start_lng : Float? = 0f
    private var end_lat : Float? = 0f
    private var end_lng : Float? = 0f

   private fun saveLocationToDb() {
        start_lat = mStartlocation.latitude.toFloat()
        start_lng = mStartlocation.longitude.toFloat()

        end_lat = mEndlocation.latitude.toFloat()
        end_lng = mEndlocation.longitude.toFloat()
       Toast.makeText(context!!,start_lat.toString() + " " + start_lng.toString(),Toast.LENGTH_LONG).show()

       val trakkData = TrakkData(start_pointLat = start_lat!!,start_pointLng = start_lng!!
                                ,end_pointLat = end_lat!!, end_pointLng = end_lng!!)
       mLastTrakkId.text = trakkData.trakkID.toString()
       mDb.trakkDao().insertTrakk(trakkData)

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
             getLastLocation()


        }else {
            if (trakkOnGoingAnim.isAnimating)
            {
                trakkOnGoingAnim.loop(false)
                trakkOnGoingAnim.pauseAnimation()}
            mLastTrakkLayout.visibility = View.VISIBLE
            mTrakkState.text = "Start New Trakk"
            mTrakkStarted = false
            requestNewLocationData()

        }
    }

    val PERMISSION_ID = 42
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
               mStartlocation = mFusedLocationManager.getLastLocation()
            }
        }
    }


lateinit var mlocation : Location

@SuppressLint("MissingPermission")
fun getLastLocation() {
    if (checkPermissions()) {
        if (isLocationEnabled()) {

            mFusedLocationClient.lastLocation.addOnCompleteListener(activity as MainActivity) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
//                    mlocation = location
                    mStartlocation = location

                    Toast.makeText(context!!,mStartlocation.latitude.toString() + " " + mStartlocation.longitude.toString(),Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context!!.startActivity(intent)
        }
    } else {
        requestPermissions()
    }
}
fun getLocation(): Location {
    return mlocation
}

@SuppressLint("MissingPermission")
private fun requestNewLocationData() {
    val mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 0
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
    mFusedLocationClient.requestLocationUpdates(
        mLocationRequest, mLocationCallback,
        Looper.myLooper()
    )
}

private val mLocationCallback = object : LocationCallback()  {
    override fun onLocationResult(locationResult: LocationResult) {
        val mLastLocation: Location = locationResult.lastLocation
        mEndlocation = mLastLocation

        saveLocationToDb()
    }
}

private fun isLocationEnabled(): Boolean {
    val locationManager: LocationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

private fun checkPermissions(): Boolean {
    if (ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

private fun requestPermissions() {
    ActivityCompat.requestPermissions(
        activity as MainActivity,
        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSION_ID
    )
}
}