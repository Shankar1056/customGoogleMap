package tyler.a3frames.myfirstapplication.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tyler.a3frames.myfirstapplication.R
import tyler.a3frames.myfirstapplication.view.Coordinate

class CoordinateAdapter (private val catlist: ArrayList<Coordinate>, private val mListener: OnItemClickListener) : RecyclerView.Adapter<CoordinateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_coordinate, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = catlist[position]

        holder.titleTV.text = "lat: "+item.fromlat+","+item.fromlon+" lon"+item.tolat+","+item.toLon
    }

    override fun getItemCount(): Int {
        return catlist.size
    }


    interface OnItemClickListener {
        fun onItemClick(item: ArrayList<Coordinate>, pos:Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTV: TextView

        init {

            titleTV = itemView.findViewById<View>(R.id.latLonTV) as TextView

            itemView.setOnClickListener {
                mListener.onItemClick(catlist, adapterPosition) }
        }
    }
}