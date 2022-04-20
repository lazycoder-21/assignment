package com.example.assignment.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.data.Repository
import com.example.assignment.data.RidesItem
import com.example.assignment.data.User
import com.example.assignment.utils.Resource
import com.example.assignment.utils.isFutureDate
import com.example.assignment.utils.isPastDate
import com.example.assignment.utils.searchClosest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(private val repository: Repository) : ViewModel() {

    private val allRides = ArrayList<RidesItem>()
    private val _nearestRides: MutableLiveData<Resource<MutableList<RidesItem>>> = MutableLiveData()
    val nearestRides: LiveData<Resource<MutableList<RidesItem>>> = _nearestRides
    private val _futureRides: MutableLiveData<Resource<MutableList<RidesItem>>> = MutableLiveData()
    val futureRides: LiveData<Resource<MutableList<RidesItem>>> = _futureRides
    private val _pastRides: MutableLiveData<Resource<MutableList<RidesItem>>> = MutableLiveData()
    val pastRides: LiveData<Resource<MutableList<RidesItem>>> = _pastRides
    private val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {

        val responseRides = repository.getRides()
        val responseUserData = repository.getUser()

        _nearestRides.value = Resource.Loading()
        _futureRides.value = Resource.Loading()
        _pastRides.value = Resource.Loading()

        responseRides.data?.let {
            allRides.addAll(it)
        } ?: kotlin.run {
            _nearestRides.value = Resource.Error(Throwable("Something went wrong"))
            _futureRides.value = Resource.Error(Throwable("Something went wrong"))
            _pastRides.value = Resource.Error(Throwable("Something went wrong"))
        }

        responseUserData.data?.let {
            _userData.value = it
        }

        getNearest()
        getFutureOrPast()
    }

    private fun getNearest() = viewModelScope.launch {

        val result = async(Dispatchers.IO) {

            //Temp list for storing result
            val list = ArrayList<RidesItem>()

            // User station code
            val userStationCode = _userData.value?.stationCode ?: 0

            // Iterating over all the ride items
            for (obj in allRides) {

                // Sorting the station codes array
                val sortedArrayList = obj.stationPath
                sortedArrayList?.sort()

                // Finding the station nearest to user and storing distance from it
                val distance =
                    kotlin.math.abs(
                        userStationCode - searchClosest(
                            userStationCode,
                            sortedArrayList!!
                        )
                    )
                list.add(obj.copy(distance = distance))
            }

            //Returning the sorted list based on the calculated distance
            list.also {
                it.sortBy { item -> item.distance }
            }
        }
        _nearestRides.value = Resource.Success(result.await())
    }

    private fun getFutureOrPast() = viewModelScope.launch {
        val futureRidesList = ArrayList<RidesItem>()
        val pastRidesList = ArrayList<RidesItem>()

        allRides.forEach { item ->
            if (isFutureDate(item.date)) {
                futureRidesList.add(item)
            }
            if (isPastDate(item.date)) {
                pastRidesList.add(item)
            }
        }

        _futureRides.value = Resource.Success(futureRidesList)
        _pastRides.value = Resource.Success(pastRidesList)

    }

    fun getStates(): List<String> {
        return allRides.map { it.state }.distinct()
    }

    fun getCities(): List<String> {
        return allRides.map { it.city }.distinct()
    }

    fun getCityFromStates(selectedState: String): List<String> {
        return allRides.filter { it.state == selectedState }.map { it.city }
    }

    fun filterNearestByState(selectedState: String) = viewModelScope.launch {
        val oldList = _nearestRides.value?.data
        _nearestRides.value = Resource.Loading()
        val filteredList = async(Dispatchers.IO) {
            oldList?.filter { it.state == selectedState }
        }
        filteredList.await()?.toMutableList()?.let {
            _nearestRides.value = Resource.Success(it)
        } ?: kotlin.run { _nearestRides.value = Resource.Error(Throwable("No rides found")) }

    }

    fun filterNearestByCity(selectedCity: String) = viewModelScope.launch {
        val oldList = _nearestRides.value?.data
        _nearestRides.value = Resource.Loading()
        val filteredList = async(Dispatchers.IO) {
            oldList?.filter { it.city == selectedCity }
        }
        filteredList.await()?.toMutableList()?.let {
            _nearestRides.value = Resource.Success(it)
        } ?: kotlin.run { _nearestRides.value = Resource.Error(Throwable("No rides found")) }
    }

}