package eu.darach.irishraillivedepartures.ui.home

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.darach.irishraillivedepartures.R
import eu.darach.irishraillivedepartures.ui.trains.TrainsActivity
import eu.darach.irishraillivedepartures.utils.RECENTLY_SEARCHED
import eu.darach.irishraillivedepartures.utils.SAVED_PREFS
import eu.darach.irishraillivedepartures.utils.STATION_EXTRA
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.recent_item.view.*


class RecentsViewAdapter(private var recentlySearched: ArrayList<String>, private var view: HomeActivity) :
    RecyclerView.Adapter<RecentsViewAdapter.RecentsViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentsViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recent_item, parent, false)
        return RecentsViewAdapter.RecentsViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecentsViewHolder, position: Int) {

        if (position < recentlySearched.size) {
            holder.recentStation.text = recentlySearched[position]
            holder.itemView.setOnClickListener {

                val intent = Intent(context, TrainsActivity::class.java)
                intent.putExtra(STATION_EXTRA, recentlySearched[position])
                addToRecents(recentlySearched[position])
                view.newActivityAnim.playAnimation()

                view.newActivityAnim.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        context.startActivity(intent)
                    }
                })
            }
        }
    }

    private fun addToRecents(searched: String) {

        val recentlySearched = getRecentlySearched()

        val it = recentlySearched.iterator()
        while (it.hasNext()) {
            val item = it.next()
            if (item == searched) {
                it.remove()
            }
        }

        val prefs = context.getSharedPreferences(SAVED_PREFS, 0)
        val editor = prefs.edit()
        val gson = Gson()

        recentlySearched.add(0, searched)

        if (recentlySearched.size > 3)
            recentlySearched.removeAt(recentlySearched.size - 1)

        val json = gson.toJson(recentlySearched)
        editor.putString(RECENTLY_SEARCHED, json)
        editor.apply()

    }

    private fun getRecentlySearched(): ArrayList<String> {

        val prefs = context.getSharedPreferences(SAVED_PREFS, 0)
        val gson = Gson()

        val json = prefs.getString(RECENTLY_SEARCHED, "")
        val list = gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type)

        return list as java.util.ArrayList<String>? ?: java.util.ArrayList()
    }

    override fun getItemCount(): Int {
        return recentlySearched.size
    }

    class RecentsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val recentStation: TextView = v.recentStation

    }

}

