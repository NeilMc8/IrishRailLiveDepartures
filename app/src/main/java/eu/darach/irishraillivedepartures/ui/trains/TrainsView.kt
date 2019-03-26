package eu.darach.irishraillivedepartures.ui.trains

import eu.darach.irishraillivedepartures.base.BaseView
import eu.darach.irishraillivedepartures.model.Train

interface TrainsView : BaseView {

    fun updateTrains(trains: List<Train>, animate: Boolean)

    fun showError(error: String)

    fun showLoading()

    fun hideLoading()

    fun cleanRv()

}