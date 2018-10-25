package tyler.a3frames.myfirstapplication.common

import android.util.Patterns
import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng


object Utilz {

    fun isValidEmail1(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches()
        }
    }

     fun getUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude


        // Sensor enabled
        val sensor = "sensor=false"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor"

        // Output format
        val output = "json"

        // Building the url to the web service
       // val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"+ "&key=AIzaSyCcf4aMDziXyyAlJ5bdAxGcWXAmoEfdHmU"
        val url = "https://maps.googleapis.com/maps/api/directions/$output?$parameters"+ "&key=AIzaSyAJWv37KIU6sB_xpcUIDRfoAhM4czLWz5o"


        return url
    }

}