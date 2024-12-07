package com.example.totravel.network

data class Country(
    val name: Name,
    val capital: List<String>?,
    val region: String,
    val population: Int,
    val area: Double,
    val flags: Flags,
    val languages: Map<String, String>?,
    val currencies: Map<String, Currency>?,
    val continents: List<String>,
    val maps: Maps,
    val demonyms: Demonyms
)

data class Name(
    val common: String,
    val official: String
)

data class Flags(
    val png: String
)

data class Currency(
    val name: String,
    val symbol: String
)

data class Maps(
    val googleMaps: String
)

data class Demonyms(
    val eng: Demonym
)

data class Demonym(
    val f: String,
    val m: String
)
