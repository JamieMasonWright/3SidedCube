package com.jj.a3sidedcube.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.jj.a3sidedcube.R
import com.jj.a3sidedcube.adapters.LoadingAdapter
import com.jj.a3sidedcube.adapters.PokemonAdapter
import com.jj.a3sidedcube.databinding.FragmentPokemonListBinding
import com.jj.a3sidedcube.utils.PRODUCT_VIEW_TYPE
import com.jj.a3sidedcube.utils.toast
import com.jj.a3sidedcube.viewmodels.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job


import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list) {

    private lateinit var binding: FragmentPokemonListBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private var job: Job? = null
    private val adapter = PokemonAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonListBinding.bind(view)

        setAdapter()

        binding.searchView.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.searchView.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })


    }

    private fun performSearch(searchString: String) {
        hideSoftKeyboard()

        if (searchString.isEmpty()) {
            requireContext().toast("Search cannot be empty")
            return
        }
        startFetchingPokemon(searchString)


    }

    private fun hideSoftKeyboard() {
        val view = requireActivity().currentFocus

        view?.let {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }


    }

    private fun setAdapter() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == PRODUCT_VIEW_TYPE) 1
                else 2
            }
        }
        binding.pokemonList.layoutManager = gridLayoutManager
        binding.pokemonList.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter { retry() }
        )

        startFetchingPokemon(null)

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading
            ) {
                binding.progressCircular.isVisible = true
                binding.textError.isVisible = false

            } else {
                binding.progressCircular.isVisible = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                if (adapter.snapshot().isEmpty()) {
                    error?.let {
                        binding.textError.visibility = View.VISIBLE
                        binding.textError.setOnClickListener {
                            adapter.retry()
                        }
                    }

                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        hideSoftKeyboard()
    }

    private fun startFetchingPokemon(searchString: String?) {
        job?.cancel()
        job = lifecycleScope.launch {
            adapter.submitData(PagingData.empty())
            viewModel.getAdverts(searchString).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun retry() {
        adapter.retry()
    }

}