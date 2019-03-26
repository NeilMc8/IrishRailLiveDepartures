package eu.darach.irishraillivedepartures.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import eu.darach.irishraillivedepartures.base.BaseView

@Module
@Suppress("unused")
object ContextModule {

    @Provides
    @JvmStatic
    internal fun provideContext(baseView: BaseView): Context {
        return baseView.getContext()
    }

    @Provides
    @JvmStatic
    internal fun provideApplication(context: Context): Application {
        return context.applicationContext as Application
    }
}