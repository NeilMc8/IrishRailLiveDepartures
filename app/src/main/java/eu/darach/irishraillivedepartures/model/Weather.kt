package eu.darach.irishraillivedepartures.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Weather {

    @SerializedName("weather")
    @Expose
    val weatherData: List<WeatherData>? = null
    @SerializedName("main")
    @Expose
    val main: Main? = null
    @SerializedName("sys")
    @Expose
    val sys: Sys? = null

}

class Main {

    @SerializedName("temp")
    @Expose
    val temp: Double? = null

}

class WeatherData {

    @SerializedName("description")
    @Expose
    val description: String? = null

}

class Sys {

    @SerializedName("sunrise")
    @Expose
    val sunrise: Int? = null
    @SerializedName("sunset")
    @Expose
    val sunset: Int? = null

}
