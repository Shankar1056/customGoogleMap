package tyler.a3frames.myfirstapplication.common

import android.os.AsyncTask
import com.google.android.gms.maps.model.PolylineOptions
import tyler.a3frames.myfirstapplication.presenter.MapPresenter
import tyler.a3frames.myfirstapplication.view.MapsActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FetchUrl(private val valu: Polyline): AsyncTask<String, Void, String>(), MapPresenter.Poliline {
    override fun setPoliline(value: PolylineOptions) {
        valu.getpolyline(value)
    }

    override fun doInBackground(vararg url: String): String {

        // For storing data from web service
        var data = ""

        try {
            // Fetching the data from web service
            data = downloadUrl(url[0])
        } catch (e: Exception) {
        }

        return data
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)

        val parserTask = ParserTask(this)

        // Invokes the thread for parsing the JSON data
       parserTask.execute(result)

    }

    interface Polyline{
        fun getpolyline(value: PolylineOptions)
    }
}


@Throws(IOException::class)
private fun downloadUrl(strUrl: String): String {
    var data = ""
    var iStream: InputStream? = null
    var urlConnection: HttpURLConnection? = null
    try {
        val url = URL(strUrl)

        // Creating an http connection to communicate with url
        urlConnection = url.openConnection() as HttpURLConnection

        // Connecting to url
        urlConnection.connect()

        // Reading data from url
        iStream = urlConnection.inputStream

        val br = BufferedReader(InputStreamReader(iStream!!))

        val sb = StringBuffer()

//        var line = ""
//            while ((line = br.readLine()) != null) {
//                sb.append(line)
//            }
        /*while(line!=null){
            line = br.readLine()
            sb.append(line)
        }*/

        var line : String?

        do {

            line = br.readLine()

            if (line == null)

                break

            sb.append(line)

        } while (true)


        data = sb.toString()
        br.close()

    } catch (e: Exception) {
    } finally {
        iStream!!.close()
        urlConnection!!.disconnect()
    }
    return data



}
