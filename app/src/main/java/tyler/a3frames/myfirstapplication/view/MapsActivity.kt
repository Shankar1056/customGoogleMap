package tyler.a3frames.myfirstapplication.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import tyler.a3frames.myfirstapplication.R
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import tyler.a3frames.myfirstapplication.common.FetchUrl
import tyler.a3frames.myfirstapplication.common.Utilz
import tyler.a3frames.myfirstapplication.listener.OnClickListenr
import tyler.a3frames.myfirstapplication.view.fragment.CoordinateFragment


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener, FetchUrl.Polyline {

    private var polylineFinal: Polyline?= null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL:Long = 10 * 1000;
    private val FASTEST_INTERVAL:Long = 2000;

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



        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLocation()
        }

        MarkerPoints = ArrayList<LatLng>()

        gotoCoordinatFragment()
    }

    private fun gotoCoordinatFragment() {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, CoordinateFragment(object : OnClickListenr {
            override fun onClick(coordinate: Coordinate) {
                mMap!!.clear()
                drawRoute(LatLng(coordinate.fromlat, coordinate.fromlon), LatLng(coordinate.tolat, coordinate.toLon))
            }
        }))
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val originLatlong = LatLng(12.956189, 77.637709)
        val destinationLatlong = LatLng(12.903000, 77.620317)
        drawRoute(originLatlong, destinationLatlong)


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
        if(checkPermissions()) {
            //LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
        }
    }

    fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)


        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSIONS_REQUEST_CODE)

        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()

            } else {

            }
        }
    }
    companion object {

        private val TAG = "MapActivity"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

}

