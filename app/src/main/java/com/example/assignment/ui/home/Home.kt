package com.example.assignment.ui.home


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.databinding.DialogBinding
import com.example.assignment.databinding.HomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : AppCompatActivity() {
    private lateinit var binding: HomeBinding
    private val viewModel: HomeVM by viewModels()
    private val adapter by lazy { HomeAdapter(this) }
    private val tabTitle = arrayOf("Nearest", "Upcoming", "Past")
    private var city = ""
    private var state = ""
    private lateinit var dialogBinding: DialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.userData.observe(this) {
            binding.apply {
                tvUserName.text = it?.name
                Glide.with(this@Home).load(it?.url).into(ivAvatar)
            }
        }

        binding.vp.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.vp) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.tvSortOrFilter.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() = try {
        val builder = AlertDialog.Builder(this)
        dialogBinding = DialogBinding.inflate(LayoutInflater.from(this), null, false)
        builder.setCancelable(true)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        var selectedCity = ""
        var selectedState = ""
        val arrayAdapterState = ArrayAdapter(this, R.layout.item_dropdown, viewModel.getStates())
        val arrayAdapterCity = if (state.isNotEmpty()) {
            ArrayAdapter(this, R.layout.item_dropdown, viewModel.getCityFromStates(state))
        } else {
            ArrayAdapter(this, R.layout.item_dropdown, viewModel.getCities())
        }
        dialogBinding.etState.setAdapter(arrayAdapterState)
        dialogBinding.etCity.setAdapter(arrayAdapterCity)
        arrayAdapterCity.setNotifyOnChange(true)
        dialogBinding.etState.setOnItemClickListener { _, _, position, _ ->
            selectedState = arrayAdapterState.getItem(position)!!
            arrayAdapterCity.clear()
            arrayAdapterCity.addAll(viewModel.getCityFromStates(selectedState))
            arrayAdapterCity.notifyDataSetChanged()
        }

        dialogBinding.etCity.setOnItemClickListener { _, _, position, _ ->
            selectedCity = arrayAdapterCity.getItem(position)!!
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            if (selectedCity.isNotEmpty() && selectedCity != city) {
                city = selectedCity
                viewModel.filterNearestByCity(selectedCity)
                return@setOnDismissListener
            }
            if (selectedState.isNotEmpty() && selectedState != state) {
                state = selectedState
                viewModel.filterNearestByState(selectedState)
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onBackPressed() {
        showExit()
    }

    private fun showExit() {
        val aD = AlertDialog.Builder(this)
        aD.setTitle(getString(R.string.exit_message))
        aD.setCancelable(false)
        aD.setPositiveButton("Ok") { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
            finishAffinity()
        }
        aD.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.cancel()
            dialogInterface.dismiss()
        }
        aD.create()
        aD.show()
    }

}