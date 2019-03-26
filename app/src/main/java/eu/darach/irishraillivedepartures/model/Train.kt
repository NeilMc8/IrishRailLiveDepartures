package eu.darach.irishraillivedepartures.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "objStationData", strict = false)
data class Train(
    @field:Element(name = "Origin") var origin: String? = null,
    @field:Element(name = "Destination") var destination: String? = null,
    @field:Element(name = "Duein") var duein: String? = null,
    @field:Element(name = "Late") var late: String? = null,
    @field:Element(name = "Expdepart") var expdepart: String? = null,
    @field:Element(name = "Schdepart") var schdepart: String? = null,
    @field:Element(name = "Direction") var direction: String? = null,
    @field:Element(name = "Traintype") var traintype: String? = null,
    @field:Element(name = "Stationfullname") var stationfullname: String? = null,
    @field:Element(name = "Querytime") var querytime: String? = null

)
