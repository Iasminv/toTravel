package com.example.totravel.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable

@Parcelize
data class Name(
    val common: String,
    val official: String
) : Parcelable

@Parcelize
data class Flags(
    val png: String
) : Parcelable

@Parcelize
data class Currency(
    val name: String,
    val symbol: String
) : Parcelable

@Parcelize
data class Maps(
    val googleMaps: String
) : Parcelable

@Parcelize
data class Demonyms(
    val eng: Demonym
) : Parcelable

@Parcelize
data class Demonym(
    val f: String,
    val m: String
) : Parcelable
