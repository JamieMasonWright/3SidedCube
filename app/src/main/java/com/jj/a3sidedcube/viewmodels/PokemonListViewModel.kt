package com.jj.a3sidedcube.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jj.a3sidedcube.data.repositories.PokemonRepository
import com.jj.a3sidedcube.domain.PokemonResult
import kotlinx.coroutines.flow.Flow

class PokemonListViewModel @ViewModelInject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {

    private var currentResult: Flow<PagingData<PokemonResult>>? = null
    fun getAdverts(searchString: String?): Flow<PagingData<PokemonResult>> {
        val newResult: Flow<PagingData<PokemonResult>> =
            pokemonRepository.getPokemon(searchString).cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }

    suspend fun getStats(id:Int) = pokemonRepository.getStats(id)
}