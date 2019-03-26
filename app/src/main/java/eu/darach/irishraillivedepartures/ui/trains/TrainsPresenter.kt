package eu.darach.irishraillivedepartures.ui.trains

import com.crashlytics.android.Crashlytics
import eu.darach.irishraillivedepartures.base.BasePresenter
import eu.darach.irishraillivedepartures.model.Train
import eu.darach.irishraillivedepartures.network.TrainApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class TrainsPresenter(trainView: TrainsView) : BasePresenter<TrainsView>(trainView) {
    @Inject
    lateinit var trainApiService: TrainApiService
    private var subscription: Disposable? = null
    var station: String = ""

    override fun onViewCreated(b: Boolean) {
        loadTrains(b)
    }

    private fun loadTrains(animate: Boolean) {
        view.showLoading()
        subscription = trainApiService
            .getTrains(station)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnTerminate { view.hideLoading() }
            .subscribe(
                { trainsArray -> trainsArray.trains?.let { sortTrains(it, animate) } },
                { e ->
                    Crashlytics.log(e.localizedMessage)
                    view.showError("No Trains Found")
                }
            )
    }

    private fun sortTrains(list: List<Train>, animate: Boolean) {

        Collections.sort(list, object : Comparator<Train> {
            override fun compare(o1: Train, o2: Train): Int {
                return o1.duein!!.toInt().compareTo(o2.duein!!.toInt())
            }
        })

        view.updateTrains(list, animate)
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}