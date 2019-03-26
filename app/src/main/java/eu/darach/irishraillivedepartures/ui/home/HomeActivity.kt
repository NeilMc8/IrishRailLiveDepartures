package eu.darach.irishraillivedepartures.ui.home

import android.animation.Animator
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import eu.darach.irishraillivedepartures.R
import eu.darach.irishraillivedepartures.base.BaseActivity
import eu.darach.irishraillivedepartures.model.ArrayOfTrainStations
import eu.darach.irishraillivedepartures.model.Weather
import eu.darach.irishraillivedepartures.ui.trains.TrainsActivity
import eu.darach.irishraillivedepartures.utils.STATION_EXTRA
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_home.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import com.google.android.material.snackbar.Snackbar




class HomeActivity : BaseActivity<HomePresenter>(), HomeView {

    private val stations: ArrayList<String> = ArrayList()
    private var recentsAdapter: RecentsViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setTimeAndDate()
        weatherLayout.visibility = View.INVISIBLE
        setLocation()
        setAnimation()

        searchBar.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            showLiveTrains()
        }

        searchBar.setOnEditorActionListener { v, actionId, event ->
            if (event != null && (event.keyCode == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                if (stations.contains(v.text.toString().toUpperCase())) {
                    showLiveTrains()
                } else {
                    showError(getString(R.string.invalid_station))
                    searchBar.text = null

                }
            }
            false

        }

        if (presenter.getRecentlySearched(this).size > 0) {
            recentsAdapter = RecentsViewAdapter(presenter.getRecentlySearched(this), this)
            recentsRv.layoutManager = LinearLayoutManager(this)
            recentsRv.adapter = recentsAdapter
        }

        presenter.onViewCreated(false)

    }

    private fun showLiveTrains() {

        val intent = Intent(this, TrainsActivity::class.java)
        intent.putExtra(STATION_EXTRA, searchBar.text.toString())
        presenter.addToRecents(searchBar.text.toString().toUpperCase(), this)
        searchBar.text = null
        newActivityAnim.playAnimation()

        newActivityAnim.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator) {
                startActivity(intent)
            }
        })
    }

    private fun setAnimation() {

        headerAnimation.apply {
            speed = 0.6f
        }

        headerAnimation.playAnimation()
    }

    private fun setLocation() {

        // getLocation can't run on main UI thread
        val thread = object : Thread() {
            override fun run() {
                val geocoder = Geocoder(getContext(), Locale.getDefault())
                var listOfAddress: List<Address>? = null
                try {

                    if (presenter.getRecentlySearched(getContext()).size > 0) {
                        listOfAddress = geocoder.getFromLocationName(
                            presenter.getRecentlySearched(getContext())[0] + getString(eu.darach.irishraillivedepartures.R.string.ireland_ext),
                            1
                        )
                    }
                } catch (e: IOException) {
                } finally {

                    if (listOfAddress != null && !listOfAddress.isEmpty()) {
                        val address = listOfAddress[0]

                        runOnUiThread {
                            if (address.locality != null) {

                                locationTv.text = address.locality
                            } else if (address.featureName != null) {
                                locationTv.text = address.featureName
                            }
                        }

                        presenter.loadWeather(listOfAddress[0].latitude, listOfAddress[0].longitude)

                    }
                }
            }
        }
        thread.start()


    }

    private fun setTimeAndDate() {

        val timeFormat = SimpleDateFormat("hh:mm a")
        val time = timeFormat.format(Date())
        timeTv.text = time.toUpperCase()

        val thread = object : Thread() {

            override fun run() {
                try {
                    while (!this.isInterrupted) {
                        Thread.sleep(999)
                        runOnUiThread {
                            val time = timeFormat.format(Date())
                            timeTv.text = time.toUpperCase()
                        }
                    }
                } catch (e: InterruptedException) {
                }

            }
        }

        thread.start()

        val dateFormat = SimpleDateFormat("E, MMMM d")
        val date = dateFormat.format(Date())
        dateTv.text = date.toUpperCase()

        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        when (timeOfDay) {
            in 0..11 -> goodTv.text = getString(eu.darach.irishraillivedepartures.R.string.good_morning)
            in 12..15 -> goodTv.text = getString(eu.darach.irishraillivedepartures.R.string.good_afternoon)
            in 16..20 -> goodTv.text = getString(eu.darach.irishraillivedepartures.R.string.good_evening)
            in 21..23 -> goodTv.text = getString(eu.darach.irishraillivedepartures.R.string.good_night)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun showError(error: String) {

        val snackbar = Snackbar
            .make(constraintLayout, error, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun initPresenter(): HomePresenter {
        return HomePresenter(this)
    }

    override fun populateStationsList(stationsArray: ArrayOfTrainStations?) {

        stationsArray!!.stations!!.forEach { stations.add(it.stationName!!.toUpperCase()) }

        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, stations)
        searchBar.setAdapter(adapter)

    }

    override fun onResume() {
        super.onResume()

        newActivityAnim.progress = 0.0F

        setTimeAndDate()
        weatherLayout.visibility = View.INVISIBLE
        setLocation()
        setAnimation()

        if (presenter.getRecentlySearched(this).size > 0) {
            recentsRv.removeAllViews()
            recentsAdapter = RecentsViewAdapter(presenter.getRecentlySearched(this), this)
            recentsRv.layoutManager = LinearLayoutManager(this)
            recentsRv.adapter = recentsAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun displayWeather(weather: Weather?) {

        if (weather != null) {
            degreeTv.text = weather.main!!.temp!!.roundToInt().toString() + getString(R.string.celcius)
            weather_icon.setImageResource(presenter.setAppropriateIcon(weather, Calendar.getInstance().time))
        }

        weatherLayout.visibility = View.VISIBLE
    }
}