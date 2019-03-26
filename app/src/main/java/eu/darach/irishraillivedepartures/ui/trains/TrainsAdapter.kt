package eu.darach.irishraillivedepartures.ui.trains

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import eu.darach.irishraillivedepartures.R
import eu.darach.irishraillivedepartures.model.Train
import kotlinx.android.synthetic.main.item_train.view.*
import java.text.SimpleDateFormat
import java.util.*


class TrainsAdapter : RecyclerView.Adapter<TrainsAdapter.TrainViewHolder>() {

    private var trains = listOf<Train>()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_train, parent, false)
        return TrainViewHolder(v)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {


        holder.destination.text =
            if (trains[position].stationfullname == trains[position].destination) "Terminates" else trains[position].destination

        holder.duein.text = trains[position].duein + context.getString(R.string.minute)

        if (Integer.parseInt(trains[position].late) > 0) {
            holder.late.visibility = View.VISIBLE
            holder.late.text = trains[position].late + context.getString(R.string.minutes_late)
        }

        if (!trains[position].expdepart.equals(trains[position].schdepart)) {
            holder.exparrival.visibility = View.VISIBLE
            holder.exparrival.text = context.getString(R.string.expected) + trains[position].expdepart
        }

        if ("00:00" == trains[position].schdepart) {

            val sdf = SimpleDateFormat("HH:mm")
            val currentDateandTime = sdf.format(Date())

            val date = sdf.parse(currentDateandTime)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MINUTE, trains[position].duein!!.toInt() - trains[position].late!!.toInt())

            holder.scharrival.text = "*" + sdf.format(calendar.time)

        } else {
            holder.scharrival.text = trains[position].schdepart
        }

        holder.origin.text = context.getString(R.string.from) + trains[position].origin
        holder.direction.text = trains[position].direction

        holder.trainView!!.setPadding(48, 48, 48, 48)

    }

    override fun getItemCount(): Int {
        return trains.size
    }

    fun updateTrains(trains: List<Train>) {
        this.trains = trains
        notifyDataSetChanged()
    }

    class TrainViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        val destination: TextView = v.destination
        val duein: TextView = v.duein
        val late: TextView = v.late
        val exparrival: TextView = v.exparrival
        val scharrival: TextView = v.scharrival
        val origin: TextView = v.origin
        val direction: TextView = v.direction
        val trainView: ConstraintLayout? = v.trainView

    }

}

