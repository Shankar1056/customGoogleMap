package tyler.a3frames.myfirstapplication.view

import com.google.android.gms.maps.model.LatLng

class MapActivityMVP {

    interface View {
        fun enableLocation()
        fun getLocation()
        fun drawRoute(origin: LatLng, destination: LatLng)
        fun getpolyline()
        fun registerLocationListner()
    }
}