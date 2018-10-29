package tyler.a3frames.myfirstapplication.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tyler.a3frames.myfirstapplication.R

class ItelList: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_call_list)
    }
}