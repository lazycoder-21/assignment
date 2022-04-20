package com.example.assignment.network

import com.example.assignment.data.RidesItem
import com.example.assignment.data.User
import com.example.assignment.network.Urls.GET_RIDES
import com.example.assignment.network.Urls.GET_USER
import retrofit2.Response
import retrofit2.http.GET

interface RidesApi {

    @GET(GET_RIDES)
    suspend fun getRides(): Response<ArrayList<RidesItem>>

    @GET(GET_USER)
    suspend fun getUser(): Response<User>

}