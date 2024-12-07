package com.example.totravel.network

import retrofit2.http.GET

interface CountryService {
    @GET("all")
    suspend fun getAllCountries(): List<Country>
}
