package com.example.totravel

import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.totravel.network.Country
import com.example.totravel.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.ArrayAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var searchField: AutoCompleteTextView
    private lateinit var searchButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var homeButton: ImageButton
    private lateinit var searchButtonNav: ImageButton
    private lateinit var calendarButton: ImageButton
    private lateinit var rndmButton: ImageButton
    private lateinit var countryList: List<Country>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        searchField = findViewById(R.id.searchField)
        searchButton = findViewById(R.id.buttonSearch)
        recyclerView = findViewById(R.id.recyclerView)
        homeButton = findViewById(R.id.homeButton)
        searchButtonNav = findViewById(R.id.searchButton)
        calendarButton = findViewById(R.id.calendarButton)
        rndmButton = findViewById(R.id.rndmButton)

        // RecyclerView setup
        countryAdapter = CountryAdapter(emptyList())
        recyclerView.adapter = countryAdapter

        // Fetch country data and setup autocomplete
        lifecycleScope.launch {
            fetchCountries()
            setupAutocomplete()
        }

        // Search button click listener
        searchButton.setOnClickListener {
            val query = searchField.text.toString()
            searchCountries(query)
        }

        // Bottom navigation functionality
        homeButton.setOnClickListener { /* Home page logic here */ }
        searchButtonNav.setOnClickListener { /* Show recent countries or advanced search options */ }

        calendarButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val randomCountry = withContext(Dispatchers.IO) { countryList.random() }
                    showCountryDetail(randomCountry)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error fetching country data", Toast.LENGTH_SHORT).show()
                }
            }
        }

        rndmButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val randomCountry = withContext(Dispatchers.IO) { countryList.random() }
                    showCountryDetail(randomCountry)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Error fetching country data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun fetchCountries() {
        try {
            countryList = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getAllCountries()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@MainActivity, "Error fetching country data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAutocomplete() {
        val countryNames = countryList.map { it.name.common }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, countryNames)
        searchField.setAdapter(adapter)
        searchField.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countryList[position]
            showCountryDetail(selectedCountry)
        }
    }

    private fun searchCountries(query: String) {
        val country = countryList.find { it.name.common.equals(query, ignoreCase = true) }
        if (country != null) {
            showCountryDetail(country)
        } else {
            Toast.makeText(this, "Country not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCountryDetail(country: Country) {
        val intent = Intent(this, CountryDetailActivity::class.java)
        intent.putExtra("country", country)
        startActivity(intent)
    }
}
