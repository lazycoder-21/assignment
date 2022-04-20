package com.example.assignment.data

import com.google.gson.annotations.SerializedName

data class ResponseRides(

    @field:SerializedName("ResponseRides")
    val responseRides: List<RidesItem?>? = null
)

data class RidesItem(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("origin_station_code")
    val originStationCode: Int? = null,

    @field:SerializedName("destination_station_code")
    val destinationStationCode: Int? = null,

    @field:SerializedName("city")
    val city: String = "",

    @field:SerializedName("station_path")
    val stationPath: ArrayList<Int>? = null,

    @field:SerializedName("map_url")
    val mapUrl: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("state")
    val state: String = "",
    val distance: Int? = 0
)
