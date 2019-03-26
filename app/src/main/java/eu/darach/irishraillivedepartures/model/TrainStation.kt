package eu.darach.irishraillivedepartures.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "objStation", strict = false)
data class TrainStation(
    @field:Element(name = "StationDesc") var stationName: String? = null
)
