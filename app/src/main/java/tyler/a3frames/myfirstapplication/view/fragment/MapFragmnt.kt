package tyler.a3frames.myfirstapplication.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Fragment
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import tyler.a3frames.myfirstapplication.R
import tyler.a3frames.myfirstapplication.common.FetchUrl
import tyler.a3frames.myfirstapplication.common.Utilz
import tyler.a3frames.myfirstapplication.R.layout.fragment_map
import com.google.android.gms.maps.SupportMapFragment



class MapFragmnt : Fragment(), OnMapReadyCallback, LocationListener, FetchUrl.Polyline {
    private var polylineFinal: Polyline? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL: Long = 10 * 1000;
    private val FASTEST_INTERVAL: Long = 2000;

    private var fusedLocationClient: FusedLocationProviderClient? = null
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    private var mMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_map, container, false)
        /*val mapFragment = activity.fragmentManager.
                findFragmentById(R.id.map) as MapFragment?
        mapFragment!!.getMapAsync(this)*/
        return view
    }

    override fun onLocationChanged(p0: Location?) {
        drawRoute(LatLng(p0!!.latitude, p0.longitude), LatLng(12.903000, 77.620317))
    }

    override fun getpolyline(value: PolylineOptions) {
        if (polylineFinal != null) {
            polylineFinal!!.remove()
        }
        polylineFinal = mMap!!.addPolyline(value)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)

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
    }

    var marker: Marker? = null
    private fun drawRoute(originLatlong: LatLng, destinationLatlong: LatLng) {

        if (marker != null) {
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
        fusedLocationClient?.lastLocation?.addOnSuccessListener(activity, OnSuccessListener { location ->
            if (location != null) {
                enableLocation()
                return@OnSuccessListener
            }


        })?.addOnFailureListener(activity) { e -> Log.w("", "getLastLocation:onFailure", e) }
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
        val settingsClient = LocationServices.getSettingsClient(activity)
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
        if (Build.VERSION.SDK_INT >= 23 && checkPermission()) {
            LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper())
        }
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions()
            return false
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, arrayOf("Manifest.permission.ACCESS_FINE_LOCATION"), 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
                registerLocationListner()
            }
        }
    }


}