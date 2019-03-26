package eu.darach.irishraillivedepartures.ui.home

import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.darach.irishraillivedepartures.R
import eu.darach.irishraillivedepartures.base.BasePresenter
import eu.darach.irishraillivedepartures.model.Weather
import eu.darach.irishraillivedepartures.network.TrainApiService
import eu.darach.irishraillivedepartures.network.WeatherApi
import eu.darach.irishraillivedepartures.utils.RECENTLY_SEARCHED
import eu.darach.irishraillivedepartures.utils.SAVED_PREFS
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class HomePresenter(homeView: HomeView) : BasePresenter<HomeView>(homeView) {

    @Inject
    lateinit var trainApiService: TrainApiService

    private var subscription: Disposable? = null

    private val apiKey = "7ec8d2f406960fa8b6ea9fb8a81cc8cc"

    override fun onViewCreated(b: Boolean) {
        loadTrainStations()
    }

    private fun loadTrainStations() {

        view.showLoading()

        subscription = trainApiService
            .getTrainStations()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnTerminate { view.hideLoading() }
            .subscribe(
                { stationsArray -> this.view.populateStationsList(stationsArray) },
                { e ->
                    Crashlytics.log(e.localizedMessage)
                    Log.e("Darach", e.localizedMessage)
                    view.showError("Network Error")
                }
            )
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun loadWeather(latitude: Double, longitude: Double) {

        subscription =
            WeatherApi().getWeatherService()?.getWeather(latitude.toString(), longitude.toString(), apiKey, "metric")
                ?.observeOn(AndroidSchedulers.mainThread())?.subscribeOn(Schedulers.io())?.subscribe(
                    { weather -> this.view.displayWeather(weather) },
                    { e ->
                        Crashlytics.log(e.localizedMessage)
                        Log.e("Darach", e.localizedMessage)
                        view.showError("Network Error")
                    }
                )

    }

    fun setAppropriateIcon(weatherObject: Weather, currentTime: Date): Int {

        if (currentTime.after(formatUnixTimeToDate(weatherObject.sys!!.sunrise)) &&
            currentTime.before(formatUnixTimeToDate(weatherObject.sys!!.sunset))
        ) {
            //daytime
            when (weatherObject.weatherData!![0].description) {
                "clear sky" -> {
                    return R.drawable.sun_icon

                }
                "few clouds" -> {
                    return R.drawable.brokencloud
                }
                "scattered clouds" -> {
                    return R.drawable.cloud
                }
                "overcast clouds" -> {
                    return R.drawable.cloud
                }
                "broken clouds" -> {
                    return R.drawable.brokencloud
                }
                "shower rain" -> {
                    return R.drawable.rain
                }
                "rain" -> {
                    return R.drawable.rain
                }
                "light rain" -> {
                    return R.drawable.rain
                }
                "thunderstorm" -> {
                    return R.drawable.thunder
                }
                "thunderstorm with light rain" -> {
                    return R.drawable.thunder
                }
                "snow" -> {
                    return R.drawable.snow
                }
                "mist" -> {
                    return R.drawable.mist
                }
            }


        } else {
            //nighttime
            when (weatherObject.weatherData!![0].description) {
                "clear sky" -> {
                    return R.drawable.night
                }
                "few clouds" -> {
                    return R.drawable.cloud_night
                }
                "overcast clouds" -> {
                    return R.drawable.cloud
                }
                "scattered clouds" -> {
                    return R.drawable.cloud
                }
                "broken clouds" -> {
                    return R.drawable.cloud_night
                }
                "shower rain" -> {
                    return R.drawable.rain_night
                }
                "rain" -> {
                    return R.drawable.rain_night
                }
                "light rain" -> {
                    return R.drawable.rain_night
                }
                "thunderstorm" -> {
                    return R.drawable.thunder_night
                }
                "thunderstorm with light rain" -> {
                    return R.drawable.thunder_night
                }
                "snow" -> {
                    return R.drawable.snow_night
                }
                "mist" -> {
                    return R.drawable.mist
                }
            }
        }
        return R.drawable.sun_icon
    }

    fun formatUnixTimeToDate(sunrise: Int?): Date {
        return Date(sunrise!! * 1000L)
    }


    fun addToRecents(searched: String, context: Context) {

        val recentlySearched = getRecentlySearched(context)

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

    fun getRecentlySearched(context: Context): ArrayList<String> {

        val prefs = context.getSharedPreferences(SAVED_PREFS, 0)
        val gson = Gson()

        val json = prefs.getString(RECENTLY_SEARCHED, "")
        val list = gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type)

        return list as ArrayList<String>? ?: ArrayList()
    }

}
