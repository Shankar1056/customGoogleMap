package tyler.a3frames.myfirstapplication.view.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cooredinate.*
import tyler.a3frames.myfirstapplication.R
import tyler.a3frames.myfirstapplication.view.Coordinate
import tyler.a3frames.myfirstapplication.view.adapter.CoordinateAdapter

class CoordinateFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_cooredinate, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        coordinateRV.layoutManager = LinearLayoutManager(activity)
        coordinateRV.adapter = CoordinateAdapter(getCoordinate(), object : CoordinateAdapter.OnItemClickListener {
            override fun onItemClick(item: ArrayList<Coordinate>, pos: Int) {

            }

            })
    }

    private fun getCoordinate(): ArrayList<Coordinate> {
        var list = ArrayList<Coordinate>()
        list.add(Coordinate("", ""))
        return list
    }
}