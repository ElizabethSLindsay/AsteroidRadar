package com.udacity.asteroidradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidEntities
import com.udacity.asteroidradar.databinding.ListItemMainBinding

class AsteroidAdapter(
    private val clickItem: (AsteroidEntities) -> Unit
) : ListAdapter<AsteroidEntities, AsteroidAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(
        private var bindingListItem: ListItemMainBinding
    ) : RecyclerView.ViewHolder(bindingListItem.root) {

        fun bindItem(asteroidEntities: AsteroidEntities) {
            bindingListItem.asteroidName
            bindingListItem.asteroidDate
            bindingListItem.asteroidStatus
            if (asteroidEntities.isPotentiallyHazardous) {
                bindingListItem.asteroidStatus.setImageResource(R.drawable.ic_status_potentially_hazardous)
            } else {
                bindingListItem.asteroidStatus.setImageResource(R.drawable.ic_status_normal)
            }
            bindingListItem.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            ListItemMainBinding.inflate(adapterLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickItem(item)
        }
        holder.bindItem(item)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<AsteroidEntities>() {
        override fun areItemsTheSame(
            oldItem: AsteroidEntities,
            newItem: AsteroidEntities
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AsteroidEntities,
            newItem: AsteroidEntities
        ): Boolean {
            return oldItem == newItem
        }

    }
}