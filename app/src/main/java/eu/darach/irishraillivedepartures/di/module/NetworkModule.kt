package eu.darach.irishraillivedepartures.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import eu.darach.irishraillivedepartures.network.TrainApiService
import eu.darach.irishraillivedepartures.utils.RAIL_BASE_URL
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Module
@Suppress("unused")
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideTrainApiService(retrofit: Retrofit): TrainApiService {
        return retrofit.create(TrainApiService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RAIL_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }
}