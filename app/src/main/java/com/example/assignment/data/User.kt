package com.example.assignment.data

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("station_code")
	val stationCode: Int? = null,

	@field:SerializedName("url")
	val url: String? = null
)
