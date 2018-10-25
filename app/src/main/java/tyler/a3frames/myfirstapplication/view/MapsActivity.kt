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
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import tyler.a3frames.myfirstapplication.common.FetchUrl
import tyler.a3frames.myfirstapplication.common.Utilz
import tyler.a3frames.myfirstapplication.presenter.MapPresenter


class MapsActivity : FragmentActivity(), OnMapReadyCallback, LocationListener, FetchUrl.Polyline {

    private var polylineFinal: Polyline?= null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL:Long = 10 * 1000;
    private val FASTEST_INTERVAL:Long = 2000;
    var isFirstTime = true

    override fun onLocationChanged(p0: Location?) {
        drawRoute(LatLng(p0!!.latitude, p0.longitude), LatLng(12.903000, 77.620317))
    }

    override fun getpolyline(value: PolylineOptions) {
        if (polylineFinal != null){
            polylineFinal!!.remove()
        }
        polylineFinal = mMap!!.addPolyline(value)
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



        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkPermission()) {
            getLocation();
        } else
            getLocation()

        MarkerPoints = ArrayList<LatLng>()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val originLatlong = LatLng(12.956189, 77.637709)
        val destinationLatlong = LatLng(12.903000, 77.620317)
       drawRoute(originLatlong, destinationLatlong)

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

    var marker: Marker?= null
    private fun drawRoute(originLatlong: LatLng, destinationLatlong: LatLng) {

        if(marker != null){
            marker!!.remove();

        }
        marker = mMap!!.addMarker(MarkerOptions().position(originLatlong))

        mMap!!.animateCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(originLatlong.latitude, originLatlong.longitude)).zoom(13f).build()))
         mMap!!.addMarker(MarkerOptions().position(destinationLatlong))
        mMap!!.animateCamera(CameraUpdateFactory
                .newCameraPosition(CameraPosition.Builder()
                        .target(LatLng(destinationLatlong.latitude, destinationLatlong.longitude)).zoom(13f).build()))

        enableLocation()
        val url = Utilz.getUrl(originLatlong, destinationLatlong)
        val FetchUrl = FetchUrl(this)
        FetchUrl.execute(url)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationClient?.lastLocation?.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location != null) {
                enableLocation()
                return@OnSuccessListener
            }


        })?.addOnFailureListener(this) { e -> Log.w("", "getLastLocation:onFailure", e) }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation() {

        mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
        mMap!!.getUiSettings().setCompassEnabled(true)
        mMap!!.setMyLocationEnabled(true)
    }


    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }
    protected fun startLocationUpdates() {
        // initialize location request object
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.run {
            setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            setInterval(UPDATE_INTERVAL)
            setFastestInterval(FASTEST_INTERVAL)
        }

        // initialize locationo setting request builder object
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // initialize location service object
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient!!.checkLocationSettings(locationSettingsRequest)

        // call register location listner
        registerLocationListner()


    }


    private fun registerLocationListner() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                onLocationChanged(locationResult!!.getLastLocation())
            }
        }
        // add permission if android version is greater then 23
        if(Build.VERSION.SDK_INT >= 23 && checkPermission()) {
            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
        }
    }
    private fun checkPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions()
            return false
        }
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"),1)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION ) {
                registerLocationListner()
            }
        }
    }

}



