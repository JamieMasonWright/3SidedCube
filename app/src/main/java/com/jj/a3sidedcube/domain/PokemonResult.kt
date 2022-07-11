package com.jj.a3sidedcube.domain

data class PokemonResult(
    val name: String,
    val url: String,
    var singlePokemonResponse: SinglePokemonResponse?
)