package eu.darach.irishraillivedepartures.ui.home

import eu.darach.irishraillivedepartures.R
import eu.darach.irishraillivedepartures.model.Sys
import eu.darach.irishraillivedepartures.model.Weather
import eu.darach.irishraillivedepartures.model.WeatherData
import org.junit.Before
import org.junit.Test


import java.util.*
import kotlin.collections.ArrayList

class HomePresenterTest {

    private val view: HomeView? = HomeActivity()
    private var presenter: HomePresenter? = null

    @Before
    fun setUp() {

        presenter = HomePresenter(this.view!!)
    }

    @Test
    fun setAppropriateIcon() {

        val weather = Weather()

        //time - positive 1
        var system = Sys()
        system.sunrise = 775904400 // 08/03/1994 @ 9:00am (UTC)
        system.sunset = 775944000 // 08/03/1994 @ 8:00pm (UTC)
        var mockTime = GregorianCalendar(1994,7,3,14,0).time
        weather.sys = system

        //weather - positive 1
        var list = ArrayList<WeatherData>()
        val snowDay = WeatherData()
        snowDay.description = "snow"
        list.add(snowDay)
        weather.weatherData = list

        var result = presenter!!.setAppropriateIcon(weather, mockTime)

        assert(result == R.drawable.snow)

        //time - positive 2
        mockTime = GregorianCalendar(1994,7,3,5,0).time
        weather.sys = system

        //weather - positive 2
        list = ArrayList()
        val rainNight = WeatherData()
        rainNight.description = "rain"
        list.add(rainNight)
        weather.weatherData = list

        result = presenter!!.setAppropriateIcon(weather, mockTime)

        assert(result == R.drawable.rain_night)

        //time - negative 1
        mockTime = GregorianCalendar(1994,7,3,5,0).time
        weather.sys = system

        //weather - negative 1
        list = ArrayList()
        val sunNight = WeatherData()
        sunNight.description = "clear sky"
        list.add(sunNight)
        weather.weatherData = list

        result = presenter!!.setAppropriateIcon(weather, mockTime)

        assert(result != R.drawable.sun_icon)

        //time - negative 2
        mockTime = GregorianCalendar(1994,7,3,13,0).time
        weather.sys = system

        //weather - negative 2
        list = ArrayList()
        val thunderstormDay = WeatherData()
        thunderstormDay.description = "thunderstorm"
        list.add(thunderstormDay)
        weather.weatherData = list

        result = presenter!!.setAppropriateIcon(weather, mockTime)

        assert(result != R.drawable.thunder_night)

    }

    @Test
    fun formatUnixTimeToDate() {

        //positive

        var expected = GregorianCalendar(1994,7,3,10,0).time
        var given = 775904400
        var result = presenter!!.formatUnixTimeToDate(given)

        assert(result == expected)

        //negative

        expected = GregorianCalendar(2006,6,9,18,0).time
        given = 1293840000
        result = presenter!!.formatUnixTimeToDate(given)

        assert(result != expected)
    }

}