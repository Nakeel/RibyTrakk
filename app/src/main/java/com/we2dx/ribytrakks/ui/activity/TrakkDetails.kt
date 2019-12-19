package com.we2dx.ribytrakks.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.we2dx.ribytrakks.R
import java.util.ArrayList
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Polyline
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.graphics.Color


class TrakkDetails : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var start_lat : Float? = 0f

    private var start_lng : Float? = 0f
    private var end_lat : Float? = 0f
    private var end_lng : Float? = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trakk_details)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        start_lat = intent.getFloatExtra("Start_lat",0f)
        start_lng = intent.getFloatExtra("Start_lng",0f)
        end_lat = intent.getFloatExtra("End_lat",0f)
        end_lng = intent.getFloatExtra("End_lat",0f)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val startLatLng = LatLng(start_lat!!.toDouble(), start_lng!!.toDouble())
        val endLatLng = LatLng(end_lat!!.toDouble(), end_lng!!.toDouble())
        addMarker(startLatLng,endLatLng)
        addPolyLine(startLatLng,endLatLng)
    }

    private fun addPolyLine(startLatLng: LatLng,endLatLng: LatLng) {
        val line = mMap.addPolyline(
            PolylineOptions()
                .add(startLatLng, endLatLng)
                .width(5f)
                .color(Color.RED)
        )
    }

    fun addMarker(startLatLng: LatLng,endLatLng: LatLng) {

        mMap.clear()
        val markerOptions = MarkerOptions()

        markerOptions.position(endLatLng)
        markerOptions.title("Going to this point")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

         //initializing and instantiating of a Array of the marker
        //which will consist of the driver and available passenger
        val markers = ArrayList<Marker>()
        markers.add(mMap.addMarker(markerOptions))
        markers.add(
            mMap.addMarker(
                MarkerOptions()
                    .position(startLatLng)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            )
        )
        //creating the Boundary that will hold just the driver and the avaiable passenger
        val builder = LatLngBounds.Builder()


        for (marker in markers) {
            builder.include(marker.position)

            //builds the boundary consisting of the available passenger and driver
            val bounds = builder.build()

            val padding = 60 // offset from edges of the map in pixels
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            mMap.animateCamera(cu)
        }

    }

}
