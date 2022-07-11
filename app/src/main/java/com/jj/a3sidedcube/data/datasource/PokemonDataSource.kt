package com.jj.a3sidedcube.data.datasource

import androidx.paging.PagingSource
import com.jj.a3sidedcube.api.PokemonApi
import com.jj.a3sidedcube.domain.PokemonResult
import com.jj.a3sidedcube.domain.SinglePokemonResponse
import com.jj.a3sidedcube.utils.STARTING_OFFSET_INDEX

import java.io.IOException


class PokemonDataSource(private val pokemonApi: PokemonApi, private val searchString: String?) :
    PagingSource<Int, PokemonResult>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonResult> {
        val offset = params.key ?: STARTING_OFFSET_INDEX
        return try {
            val data = pokemonApi.getPokemons(params.loadSize, offset)
            data.results.forEach {
                val id = it.url.substringAfter("pokemon").replace("/", "").toInt()

                val singlePokemonResponse = getSinglePokemon(id, pokemonApi)
                it.singlePokemonResponse = singlePokemonResponse
            }
            val filteredData: List<PokemonResult> = if (searchString != null) {
                data.results.filter { it.name.contains(searchString, true) }
            } else {
                data.results
            }
            LoadResult.Page(
                data = filteredData,
                prevKey = if (offset == STARTING_OFFSET_INDEX) null else offset - params.loadSize,
                nextKey = if (data.next == null) null else offset + params.loadSize
            )
        } catch (t: Throwable) {
            var exception = t

            if (t is IOException) {
                exception = IOException("Please check internet connection")
            }
            LoadResult.Error(exception)
        }
    }

    private suspend fun getSinglePokemon(id: Int, pokemonApi: PokemonApi): SinglePokemonResponse? {
        return try {
            pokemonApi.getSinglePokemon(id)
        } catch (t: Throwable) {
            null
        }
    }

}