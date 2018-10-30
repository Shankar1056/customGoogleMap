package tyler.a3frames.myfirstapplication.view.fragment

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cooredinate.*
import tyler.a3frames.myfirstapplication.R
import tyler.a3frames.myfirstapplication.listener.OnClickListenr
import tyler.a3frames.myfirstapplication.view.Coordinate
import tyler.a3frames.myfirstapplication.view.adapter.CoordinateAdapter

@SuppressLint("ValidFragment")
class CoordinateFragment @SuppressLint("ValidFragment") constructor(private val param: OnClickListenr) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_cooredinate, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        coordinateRV.layoutManager = LinearLayoutManager(activity)
        coordinateRV.adapter = CoordinateAdapter(getCoordinate(), object : CoordinateAdapter.OnItemClickListener {
            override fun onItemClick(item: ArrayList<Coordinate>, pos: Int) {
                param.onClick(item[pos])

            }

            })
    }

    private fun getCoordinate(): ArrayList<Coordinate> {
        var list = ArrayList<Coordinate>()
        list.add(Coordinate(12.953430, 77.639079, 12.957361, 77.637191))
        list.add(Coordinate( 12.956106, 77.649379, 12.957361, 77.637191))
        list.add(Coordinate( 12.956106, 77.649379, 12.953430, 77.639079))
        return list
    }
}