package eu.darach.irishraillivedepartures.network

import eu.darach.irishraillivedepartures.model.ArrayOfTrainStations
import eu.darach.irishraillivedepartures.model.ArrayOfTrains
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainApiService {

    @GET("getAllStationsXML")
    fun getTrainStations(): Observable<ArrayOfTrainStations>

    @GET("getStationDataByNameXML?")
    fun getTrains(@Query("StationDesc") stationName: String): Observable<ArrayOfTrains>

}