package tyler.a3frames.myfirstapplication.view

import android.Manifest
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import tyler.a3frames.myfirstapplication.R
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.content.DialogInterface
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import tyler.a3frames.myfirstapplication.common.FetchUrl
import tyler.a3frames.myfirstapplication.common.Utilz
import tyler.a3frames.myfirstapplication.presenter.MapPresenter


class MapsActivity : FragmentActivity(), OnMapReadyCallback, FetchUrl.Polyline {
    override fun getpolyline(value: PolylineOptions) {
        mMap!!.addPolyline(value)
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var REQUEST_LOCATION_CODE = 101
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else
                checkLocationPermission()
        } else
            getLocation()

        MarkerPoints = ArrayList<LatLng>()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val originLatlong = LatLng(12.956189, 77.637709)
        val destinationLatlong = LatLng(12.903000,77.620317)
       mMap!!.addMarker(MarkerOptions().position(originLatlong).title("3Frames"))
        mMap!!.animateCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(originLatlong.latitude, originLatlong.longitude)).zoom(15f).build()))

         mMap!!.addMarker(MarkerOptions().position(destinationLatlong).title("ViratNagar"))
        mMap!!.animateCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(destinationLatlong.latitude, destinationLatlong.longitude)).zoom(15f).build()))

        val url = Utilz.getUrl(originLatlong, destinationLatlong)
        val FetchUrl = FetchUrl(this)
        FetchUrl.execute(url)

        /*mMap!!.setOnMapClickListener { point ->
            if (MarkerPoints.size > 1) {
                MarkerPoints.clear()
                mMap!!.clear()
            }
            MarkerPoints.add(point)
            val options = MarkerOptions()
            options.position(point)

            if (MarkerPoints.size == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMap!!.animateCamera(CameraUpdateFactory
                        .newCameraPosition(CameraPosition.Builder()
                                .target(LatLng(point.latitude, point.longitude)).zoom(15f).build()))
            } else if (MarkerPoints.size == 2) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                mMap!!.animateCamera(CameraUpdateFactory
                        .newCameraPosition(CameraPosition.Builder()
                                .target(LatLng(point.latitude, point.longitude)).zoom(15f).build()))
            }

            mMap!!.addMarker(options)
            if (MarkerPoints.size >= 2) {
                val origin = MarkerPoints[0]
                val dest = MarkerPoints[1]

                val url = Utilz.getUrl(origin, dest)
                Log.d("onMapClick", url.toString())
                val FetchUrl = FetchUrl(this)

                FetchUrl.execute(url)
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(origin))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
            }
        }*/

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location != null) {
                mMap!!.animateCamera(CameraUpdateFactory
                        .newCameraPosition(CameraPosition.Builder()
                                .target(LatLng(location.latitude, location.longitude)).zoom(15f).build()))
                mMap!!.setMyLocationEnabled(true)
                mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
                mMap!!.getUiSettings().setCompassEnabled(true)
                return@OnSuccessListener
            }


        })?.addOnFailureListener(this) { e -> Log.w("", "getLastLocation:onFailure", e) }
    }
        private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                mMap!!.setMyLocationEnabled(true)
                mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
                mMap!!.getUiSettings().setCompassEnabled(true)
                AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
                        })
                        .create()
                        .show()

            } else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


}



