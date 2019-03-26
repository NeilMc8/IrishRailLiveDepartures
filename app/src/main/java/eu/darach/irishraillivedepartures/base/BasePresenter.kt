package eu.darach.irishraillivedepartures.base

import eu.darach.irishraillivedepartures.di.component.DaggerPresenterInjector
import eu.darach.irishraillivedepartures.di.component.PresenterInjector
import eu.darach.irishraillivedepartures.di.module.ContextModule
import eu.darach.irishraillivedepartures.di.module.NetworkModule
import eu.darach.irishraillivedepartures.ui.home.HomePresenter
import eu.darach.irishraillivedepartures.ui.trains.TrainsPresenter

abstract class BasePresenter<out V : BaseView>(protected val view: V) {
    private val injector: PresenterInjector = DaggerPresenterInjector
        .builder()
        .baseView(view)
        .contextModule(ContextModule)
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    open fun onViewCreated(b: Boolean) {}

    open fun onViewDestroyed() {}

    private fun inject() {
        when (this) {
            is TrainsPresenter -> injector.inject(this)
            is HomePresenter -> injector.inject(this)
        }
    }
}