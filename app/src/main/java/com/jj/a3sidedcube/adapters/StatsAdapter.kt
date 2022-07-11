package com.jj.a3sidedcube.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jj.a3sidedcube.databinding.StatItemPokemonBinding
import com.jj.a3sidedcube.domain.Stats
import com.jj.a3sidedcube.utils.MAX_BASE_STATE


class StatsAdapter :
    RecyclerView.Adapter<StatsAdapter.CartViewHolder>() {

    private val stats = ArrayList<Stats>()

    fun setStats(newList: ArrayList<Stats>) {
        stats.clear()
        stats.addAll(newList)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        return CartViewHolder(
            StatItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(stats[position])
    }

    override fun getItemCount(): Int {
        return stats.size
    }


    class CartViewHolder(private val binding: StatItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stat: Stats) {
            binding.apply {
                val mProgress = progressCircular
                mProgress.progress = stat.base_stat
                mProgress.secondaryProgress = MAX_BASE_STATE
                mProgress.max = MAX_BASE_STATE

                statName.text = stat.stat.name
                statCount.text = stat.base_stat.toString()
            }
        }
    }
}