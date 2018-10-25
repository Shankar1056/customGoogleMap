package tyler.a3frames.myfirstapplication.presenter

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import com.google.android.gms.maps.model.PolylineOptions

class MapPresenter {

    var polilineValue: PolylineOptions?= null
    interface Poliline {

        fun setPoliline(value: PolylineOptions)
    }



}