package com.example.assignment.ui.rides

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment.R
import com.example.assignment.data.RidesItem
import com.example.assignment.databinding.RidesBinding
import com.example.assignment.ui.home.HomeVM
import com.example.assignment.utils.Resource
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Rides : Fragment(R.layout.rides) {
    private lateinit var binding: RidesBinding
    private val viewModel: HomeVM by activityViewModels()
    private val ridesAdapter by lazy(LazyThreadSafetyMode.NONE) { RidesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RidesBinding.bind(view)

        binding.apply {
            rvRides.adapter = ridesAdapter
            rvRides.setHasFixedSize(true)
        }

        val liveDataToObserve = when (arguments?.getInt(POSITION_ARG)) {
            0 -> {
                viewModel.nearestRides
            }
            1 -> {
                viewModel.futureRides
            }
            2 -> {
                viewModel.pastRides
            }
            else -> {
                viewModel.nearestRides
            }
        }

        liveDataToObserve.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    binding.rvRides.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(requireView(), it.error?.message ?: "", LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {
                    binding.rvRides.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.rvRides.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    ridesAdapter.submitList(it.data)
                }
            }
        }
    }

    companion object {
        var POSITION_ARG = "position"

        @JvmStatic
        fun newInstance(position: Int) = Rides().apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }

}