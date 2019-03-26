package eu.darach.irishraillivedepartures.network

import eu.darach.irishraillivedepartures.model.Weather
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class WeatherApi {

    interface WeatherService {

        @GET("weather?")
        fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("appid") appid: String, @Query("units") units: String): Observable<Weather>

    }

    private val baseUrl = "http://api.openweathermap.org/data/2.5/"

    fun getWeatherService(): WeatherService? {

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WeatherService::class.java)

    }

}
