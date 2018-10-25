package tyler.a3frames.myfirstapplication.common

import android.graphics.Color
import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import tyler.a3frames.myfirstapplication.presenter.MapPresenter
import android.text.method.TextKeyListener.clear



class ParserTask(private val option: MapPresenter.Poliline): AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

    // Parsing the data in non-ui thread
    override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>> {

        val jObject: JSONObject

        try {
            jObject = JSONObject(jsonData[0])
            val parser = DataParser()

            // Starts parsing data
            var routes: List<List<HashMap<String, String>>>  = parser.parse(jObject)
            return routes

        } catch (e: Exception) {
            e.printStackTrace()
        }

        val r:List<List<HashMap<String, String>>> = ArrayList<ArrayList<HashMap<String, String>>>()
        return r
    }

    // Executes in UI thread, after the parsing process
    override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
        var points: ArrayList<LatLng>
        var lineOptions: PolylineOptions? = null

        // Traversing through all the routes
        for (i in result.indices) {
            points = ArrayList<LatLng>()
            lineOptions = PolylineOptions()

            // Fetching i-th route
            val path = result[i]

            // Fetching all the points in i-th route
            for (j in path.indices) {
                val point = path[j]

                val lat = java.lang.Double.parseDouble(point["lat"])
                val lng = java.lang.Double.parseDouble(point["lng"])
                val position = LatLng(lat, lng)

                lineOptions.add(position)
            }



            // Adding all the points in the route to LineOptions
//            lineOptions.addAll(points)
            lineOptions.width(10f)
            lineOptions.geodesic(true)
            lineOptions.color(Color.BLUE)


        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            option.setPoliline(lineOptions)
//            mMap!!.addPolyline(lineOptions)
        } else {
        }
    }
}