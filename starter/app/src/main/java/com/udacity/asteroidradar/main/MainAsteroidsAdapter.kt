package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainAsteroidsAdapter(val clickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, MainAsteroidsAdapter.MainAsteroidViewHolder>(AsteroidDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAsteroidViewHolder {
        val binding: AsteroidItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.asteroid_item, parent,false)
        return MainAsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAsteroidViewHolder, position: Int) {
        holder.bind(clickListener,getItem(position))
    }

    class MainAsteroidViewHolder (private val binding: AsteroidItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: AsteroidClickListener, asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
}

class AsteroidClickListener(val clickListener: (asteroid:Asteroid) -> Unit){
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

