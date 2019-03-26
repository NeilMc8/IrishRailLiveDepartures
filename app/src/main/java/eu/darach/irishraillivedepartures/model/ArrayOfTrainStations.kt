package eu.darach.irishraillivedepartures.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ArrayOfObjStation", strict = false)
data class ArrayOfTrainStations @JvmOverloads constructor(
    @field:ElementList(name = "objStation", inline = true) var stations: List<TrainStation>? = null
)
