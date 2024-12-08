package com.example.totravel

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.totravel.network.Country
import com.example.totravel.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var searchField: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var homeButton: ImageButton
    private lateinit var searchButtonNav: ImageButton
    private lateinit var calendarButton: ImageButton
    private lateinit var rndmButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Search functionality
        searchField = findViewById(R.id.searchField)
        searchButton = findViewById(R.id.buttonSearch)
        recyclerView = findViewById(R.id.recyclerView)
        countryAdapter = CountryAdapter(emptyList())
        recyclerView.adapter = countryAdapter

        searchButton.setOnClickListener {
            val query = searchField.text.toString()
            searchCountries(query)
        }

        // Bottom navigation functionality
        homeButton = findViewById(R.id.homeButton)
        searchButtonNav = findViewById(R.id.searchButton)
        calendarButton = findViewById(R.id.calendarButton)
        rndmButton = findViewById(R.id.rndmButton)

        homeButton.setOnClickListener {
            // Home page logic here
        }

        searchButtonNav.setOnClickListener {
            // Show recent countries or advanced search options
        }

        calendarButton.setOnClickListener {
            lifecycleScope.launch {
                val randomCountry = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllCountries().random()
                }
                showCountryDetail(randomCountry)
            }
        }

        rndmButton.setOnClickListener {
            lifecycleScope.launch {
                val randomCountry = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getAllCountries().random()
                }
                showCountryDetail(randomCountry)
            }
        }
    }

    private fun searchCountries(query: String) {
        lifecycleScope.launch {
            val countries = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getAllCountries().filter { it.name.common.contains(query, ignoreCase = true) }
            }
            countryAdapter.updateData(countries)
        }
    }

    private fun showCountryDetail(country: Country) {
        val intent = Intent(this, CountryDetailActivity::class.java)
        intent.putExtra("country", country)
        startActivity(intent)
    }
}
