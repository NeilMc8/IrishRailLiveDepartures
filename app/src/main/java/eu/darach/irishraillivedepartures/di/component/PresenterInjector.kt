package eu.darach.irishraillivedepartures.di.component

import dagger.BindsInstance
import dagger.Component
import eu.darach.irishraillivedepartures.base.BaseView
import eu.darach.irishraillivedepartures.di.module.ContextModule
import eu.darach.irishraillivedepartures.di.module.NetworkModule
import eu.darach.irishraillivedepartures.ui.home.HomePresenter
import eu.darach.irishraillivedepartures.ui.trains.TrainsPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = [(ContextModule::class), (NetworkModule::class)])
interface PresenterInjector {

    fun inject(trainsPresenter: TrainsPresenter)

    fun inject(homePresenter: HomePresenter)

    @Component.Builder
    interface Builder {
        fun build(): PresenterInjector

        fun networkModule(networkModule: NetworkModule): Builder
        fun contextModule(contextModule: ContextModule): Builder

        @BindsInstance
        fun baseView(baseView: BaseView): Builder
    }
}