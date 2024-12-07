package com.example.totravel.network

data class Country(
    val name: String,
    val capital: String?,
    val region: String,
    val population: Int,
    val flags: Flags
)

data class Flags(
    val png: String
)
