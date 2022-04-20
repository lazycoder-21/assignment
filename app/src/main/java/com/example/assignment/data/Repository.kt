package com.example.assignment.data

import android.app.Application
import com.example.assignment.network.RidesApi
import com.example.assignment.utils.Resource
import com.example.assignment.utils.isNetworkAvailable
import com.google.gson.JsonElement
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: RidesApi,
    private val application: Application
) {

    suspend fun getRides(): Resource<ArrayList<RidesItem>> {
        if (application.isNetworkAvailable()) {
            val response = api.getRides()
            response.body()?.let { data ->
                return Resource.Success(data)
            } ?: return Resource.Error(Throwable(message = "Something went wrong!!"))
        } else {
            return Resource.Error(Throwable(message = "No internet connection!!"))
        }
    }

    suspend fun getUser(): Resource<User> {
        if (application.isNetworkAvailable()) {
            val response = api.getUser()
            response.body()?.let { data ->
                return Resource.Success(data)
            } ?: return Resource.Error(Throwable(message = "Something went wrong!!"))
        } else {
            return Resource.Error(Throwable(message = "No internet connection!!"))
        }
    }

}