package com.example.assignment.ui.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment.ui.rides.Rides


class HomeAdapter(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return Rides.newInstance(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

}