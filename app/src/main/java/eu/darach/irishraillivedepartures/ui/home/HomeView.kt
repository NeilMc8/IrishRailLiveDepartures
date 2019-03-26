package eu.darach.irishraillivedepartures.ui.home

import eu.darach.irishraillivedepartures.base.BaseView
import eu.darach.irishraillivedepartures.model.ArrayOfTrainStations
import eu.darach.irishraillivedepartures.model.Weather

interface HomeView : BaseView {

    fun showError(error: String)

    fun showLoading()

    fun hideLoading()

    fun populateStationsList(stationsArray: ArrayOfTrainStations?)

    fun displayWeather(weather: Weather?)

}