package eu.darach.irishraillivedepartures.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Weather {

    @SerializedName("weather")
    @Expose
    var weatherData: List<WeatherData>? = null
    @SerializedName("main")
    @Expose
    val main: Main? = null
    @SerializedName("sys")
    @Expose
    var sys: Sys? = null

}

class Main {

    @SerializedName("temp")
    @Expose
    val temp: Double? = null

}

class WeatherData {

    @SerializedName("description")
    @Expose
    var description: String? = null

}

class Sys {

    @SerializedName("sunrise")
    @Expose
    var sunrise: Int? = null
    @SerializedName("sunset")
    @Expose
    var sunset: Int? = null

}
