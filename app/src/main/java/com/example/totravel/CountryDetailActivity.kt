package com.example.totravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.totravel.network.Country
import com.example.totravel.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CountryDetailActivity : AppCompatActivity() {
    private lateinit var homeButton: ImageButton
    private lateinit var searchButton: ImageButton
    private lateinit var calendarButton: ImageButton
    private lateinit var rndmButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        homeButton = findViewById(R.id.homeButton)
        searchButton = findViewById(R.id.searchButton)
        calendarButton = findViewById(R.id.calendarButton)
        rndmButton = findViewById(R.id.rndmButton)

        homeButton.setOnClickListener {
            navigateToHome()
        }

        searchButton.setOnClickListener {
            navigateToHome()
        }

        calendarButton.setOnClickListener {
            showCountryOfTheDay()
        }

        rndmButton.setOnClickListener {
            showRandomCountry()
        }

        // Your existing code for displaying country details
        val country = intent.getParcelableExtra<Country>("country")
        country?.let { displayCountryDetails(it) }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showCountryOfTheDay() {
        lifecycleScope.launch {
            try {
                val countries = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllCountries()
                }
                val calendar = Calendar.getInstance()
                val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                val countryOfTheDay = countries[dayOfYear % countries.size]
                showCountryDetail(countryOfTheDay)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showRandomCountry() {
        lifecycleScope.launch {
            try {
                val countries = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllCountries()
                }
                val randomCountry = countries.random()
                showCountryDetail(randomCountry)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showCountryDetail(country: Country) {
        val intent = Intent(this, CountryDetailActivity::class.java)
        intent.putExtra("country", country)
        startActivity(intent)
    }

    private fun displayCountryDetails(country: Country) {
        // Your existing code to display the details
        // For example:
        findViewById<TextView>(R.id.countryName).text = country.name.common
        findViewById<TextView>(R.id.countryOfficialName).text = "Official Name: ${country.name.official}"
        findViewById<TextView>(R.id.countryCapital).text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"
        findViewById<TextView>(R.id.countryRegion).text = "Region: ${country.region}"
        findViewById<TextView>(R.id.countryPopulation).text = "Population: ${country.population}"
        findViewById<TextView>(R.id.countryLanguage).text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"
        findViewById<TextView>(R.id.countryCurrency).text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"
        findViewById<TextView>(R.id.countryContinent).text = "Continent: ${country.continents.joinToString(", ")}"
        findViewById<TextView>(R.id.countryLandArea).text = "Land Area: ${country.area} km²"
        findViewById<TextView>(R.id.countryDrivingSide).text = "Driving Side: ${country.demonyms.eng.m}"
        Glide.with(this).load(country.flags.png).into(findViewById(R.id.countryFlag))
        findViewById<ImageView>(R.id.countryMap).setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(country.maps.googleMaps))
            startActivity(mapIntent)
        }
    }
}
