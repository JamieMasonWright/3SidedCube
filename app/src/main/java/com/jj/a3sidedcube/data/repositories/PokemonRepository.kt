package com.jj.a3sidedcube.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.jj.a3sidedcube.api.PokemonApi
import com.jj.a3sidedcube.data.datasource.PokemonDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(private val pokemonApi: PokemonApi) {

    fun getPokemon(searchString: String?) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 10),
        pagingSourceFactory = {
            PokemonDataSource(pokemonApi, searchString)
        }
    ).flow
}