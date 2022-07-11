package com.jj.a3sidedcube.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jj.a3sidedcube.R
import com.jj.a3sidedcube.adapters.StatsAdapter
import com.jj.a3sidedcube.databinding.FragmentPokemonStatsBinding
import com.jj.a3sidedcube.domain.Stats

class PokemonStatsFragment : Fragment(R.layout.fragment_pokemon_stats) {

    private lateinit var binding: FragmentPokemonStatsBinding
    private val adapter = StatsAdapter()
    private val args = PokemonStatsFragmentArgs


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonStatsBinding.bind(view)

        val pokemonResult = arguments?.let { args.fromBundle(it).pokemonResult }!!

        val toolbar = binding.toolbar as Toolbar
        toolbar.elevation = 0.0F
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.title = pokemonResult.name
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)

        toolbar.setNavigationOnClickListener {
            binding.root.findNavController().navigateUp()
        }

        binding.apply {
            (pokemonResult.singlePokemonResponse?.weight.toString() + " metres").also {
                pokemonItemWeight.text = it
            }
            (pokemonResult.singlePokemonResponse?.height.toString() + " Kgs").also {
                pokemonItemHeight.text = it
            }

            pokemonStatList.adapter = adapter
            adapter.setStats(pokemonResult.singlePokemonResponse?.stats as ArrayList<Stats>)

            Glide.with(root)
                .load(pokemonResult.singlePokemonResponse?.sprites?.front_default)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(pokemonItemImage)
        }
    }
}