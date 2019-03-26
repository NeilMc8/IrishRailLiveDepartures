package eu.darach.irishraillivedepartures.model

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ArrayOfObjStationData", strict = false)
data class ArrayOfTrains @JvmOverloads constructor(
    @field:ElementList(name = "objStationData", inline = true) var trains: List<Train>? = null
)
