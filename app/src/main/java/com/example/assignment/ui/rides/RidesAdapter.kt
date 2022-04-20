package com.example.assignment.ui.rides

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignment.R
import com.example.assignment.data.RidesItem
import com.example.assignment.databinding.ItemRideBinding
import com.example.assignment.utils.formatDate

class RidesAdapter() :
    ListAdapter<RidesItem, RidesAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding =
            ItemRideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ItemRideBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ride: RidesItem) {
            binding.apply {
                tvRideId.text = ride.id.toString()
                tvOriginStationId.text = ride.originStationCode.toString()
                tvStationPath.text = StringBuilder().apply {
                    append("[")
                    ride.stationPath?.forEach {
                        append(it)
                        if (it != ride.stationPath.last())
                            append(", ")
                    }
                    append("]")
                }
                tvDate.text = formatDate(ride.date)
                Glide.with(root).load(ride.mapUrl)
                    .placeholder(R.drawable.image)
                    .into(ivMap)
                tvCityName.text = ride.city
                tvStateName.text = ride.state
                tvDistance.text = ride.distance.toString()
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<RidesItem>() {
        override fun areItemsTheSame(oldItem: RidesItem, newItem: RidesItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RidesItem, newItem: RidesItem) =
            oldItem == newItem
    }
}