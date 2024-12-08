package com.example.totravel.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val name: Name,
    val capital: List<String>?,
    val region: String,
    val population: Int,
    val languages: Map<String, String>?,
    val currencies: Map<String, Currency>?,
    val continents: List<String>,
    val area: Double,
    val demonyms: Demonyms,
    val flags: Flag,
    val maps: Maps,
    val car: Car?
) : Parcelable

@Parcelize
data class Car(val side: String?) : Parcelable

@Parcelize
data class Name(val common: String, val official: String) : Parcelable

@Parcelize
data class Currency(val name: String, val symbol: String) : Parcelable

@Parcelize
data class Demonyms(val eng: Eng) : Parcelable

@Parcelize
data class Eng(val m: String, val f: String) : Parcelable

@Parcelize
data class Flag(val png: String, val alt: String?) : Parcelable

@Parcelize
data class Maps(val googleMaps: String) : Parcelable
