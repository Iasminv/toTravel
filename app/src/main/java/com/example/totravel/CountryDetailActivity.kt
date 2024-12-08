package com.example.totravel

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import kotlin.collections.MutableList
import kotlin.random.Random
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        val country = intent.getParcelableExtra<Country>("country")
        country?.let {
            displayCountryDetails(it)
        }
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

        val countryColours: MutableList<String> = mutableListOf()
        val flagColours = listOf("black", "white", "crimson", "red", "cobalt blue", "dark blue", "sky-blue", "navy blue", "light blue", "blue", "golden-yellow", "gold", "yellow", "dark green", "light green", "green", "ultramarine", "orange", "turquoise", "teal", "maroon", "saffron", "aquamarine")

        val flagDesc = country.flags.alt
    if (!flagDesc.isNullOrEmpty()){
        flagColours.forEach { colour ->
            if (flagDesc.contains(colour)) {
                when (colour) {
                    "blue" -> if (!countryColours.any { it.contains("blue") }) {
                        countryColours.add(colour)
                    }
                    "gold" -> if (!countryColours.contains("golden-yellow")) {
                        countryColours.add(colour)
                    }
                    "yellow" -> if (!countryColours.any { it == "golden-yellow" || it == "gold" }) {
                        countryColours.add(colour)
                    }
                    "green" -> if (!countryColours.any { it.contains("green") }) {
                        countryColours.add(colour)
                    }
                    "red" -> if (!countryColours.any { it == "crimson" || it.contains("\\bred\\b") }) {
                        countryColours.add(colour)
                    }
                    else -> if (!countryColours.contains(colour)) {
                        countryColours.add(colour)
                    }
                }
            }
        }
        }

        val editedCountryColours: MutableList<String> = mutableListOf()
        if (countryColours.isNotEmpty()) {
            editedCountryColours.addAll(countryColours.map { colour ->
                var modifiedColour = colour
                if (modifiedColour.contains(" ")) {
                    modifiedColour = modifiedColour.replace(" ", "_")
                }
                if (modifiedColour == "white") {
                    modifiedColour = "grey"
                }
                modifiedColour
            })
        }

        findViewById<TextView>(R.id.countryName).text = country.name.common

        //call appropriate function
        if (editedCountryColours.size >= 4){
            fourColours(country,editedCountryColours)
        }
        if (editedCountryColours.size == 3){
            threeColours(country,editedCountryColours)
        }
        if (editedCountryColours.size == 2){
            twoColours(country,editedCountryColours)
        }
        if (countryColours.size == 0){
            noColoursListed(country)
        }


        Glide.with(this).load(country.flags.png).into(findViewById(R.id.countryFlag))

        // Set the Google Maps link
        val mapLinkTextView = findViewById<TextView>(R.id.countryMapLink)
        mapLinkTextView.text = "View on Google Maps"
        mapLinkTextView.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(country.maps.googleMaps))
            startActivity(mapIntent)
        }
    }//end displayCountryDetails

    private fun noColoursListed(country: Country){
        findViewById<TextView>(R.id.countryOfficialName).text = "Official Name: ${country.name.official}"

        findViewById<TextView>(R.id.countryCapital).text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"

        findViewById<TextView>(R.id.countryRegion).text = "Region: ${country.region}"

        findViewById<TextView>(R.id.countryLanguage).text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"

        findViewById<TextView>(R.id.countryCurrency).text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"

        findViewById<TextView>(R.id.countryContinent).text = "Continent: ${country.continents.joinToString(", ")}"

        findViewById<TextView>(R.id.countryPopulation).text = "Population: ${country.population}"

        findViewById<TextView>(R.id.countryLandArea).text = "Land area: ${country.area} km²"

        val drivingSide = country.car?.side
        val capitalizedDrivingSide = drivingSide?.capitalize(Locale.getDefault())  // Capitalize first letter

        findViewById<TextView>(R.id.countryDrivingSide).text = "Drives on: ${capitalizedDrivingSide ?: "Unknown"}"
    }

    private fun twoColours(country: Country, editedCountryColours: MutableList<String>){
        val colour1 = editedCountryColours[0]
        val colour2 = editedCountryColours[1]

        //name
        findViewById<TextView>(R.id.countryOfficialName).apply{
            text = "Official Name: ${country.name.official}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //capital
        findViewById<TextView>(R.id.countryCapital).apply{
            text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }

        //region
        findViewById<TextView>(R.id.countryRegion).apply{
            text = "Region: ${country.region}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //population
        findViewById<TextView>(R.id.countryPopulation).apply{
            text = "Population: ${country.population}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //language
        findViewById<TextView>(R.id.countryLanguage).apply{
            text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }

        //currency
        findViewById<TextView>(R.id.countryCurrency).apply{
            text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //continent
        findViewById<TextView>(R.id.countryContinent).apply{
            text = "Continent: ${country.continents.joinToString(", ")}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }


        //land area
        findViewById<TextView>(R.id.countryLandArea).apply{
            text = "Land area: ${country.area} km²"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }


        //driving side
        val drivingSide = country.car?.side
        val capitalizedDrivingSide = drivingSide?.capitalize(Locale.getDefault())  // Capitalize first letter

        findViewById<TextView>(R.id.countryDrivingSide).apply{
            text = "Drives on: ${capitalizedDrivingSide ?: "Unknown"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

    }

    private fun threeColours(country: Country, editedCountryColours: MutableList<String>){
        val colour1 = editedCountryColours[0]
        val colour2 = editedCountryColours[1]
        val colour3 = editedCountryColours[2]

        //name
        findViewById<TextView>(R.id.countryOfficialName).apply{
            text = "Official Name: ${country.name.official}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //capital
        findViewById<TextView>(R.id.countryCapital).apply{
            text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }

        //region
        findViewById<TextView>(R.id.countryRegion).apply{
            text = "Region: ${country.region}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour3, "color", context.packageName)))
        }

        //population
        findViewById<TextView>(R.id.countryPopulation).apply{
            text = "Population: ${country.population}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //language
        findViewById<TextView>(R.id.countryLanguage).apply{
            text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //currency
        findViewById<TextView>(R.id.countryCurrency).apply{
            text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }

        //continent
        findViewById<TextView>(R.id.countryContinent).apply{
            text = "Continent: ${country.continents.joinToString(", ")}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour3, "color", context.packageName)))
        }


        //land area
        findViewById<TextView>(R.id.countryLandArea).apply{
            text = "Land area: ${country.area} km²"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }


        //driving side
        val drivingSide = country.car?.side
        val capitalizedDrivingSide = drivingSide?.capitalize(Locale.getDefault())  // Capitalize first letter

        findViewById<TextView>(R.id.countryDrivingSide).apply{
            text = "Drives on: ${capitalizedDrivingSide ?: "Unknown"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour3, "color", context.packageName)))
        }
    }

    private fun fourColours(country: Country, editedCountryColours: MutableList<String>){
        val colour1 = editedCountryColours[0]
        val colour2 = editedCountryColours[1]
        val colour3 = editedCountryColours[2]
        val colour4 = editedCountryColours[3]

        //name
        findViewById<TextView>(R.id.countryOfficialName).apply{
            text = "Official Name: ${country.name.official}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //capital
        findViewById<TextView>(R.id.countryCapital).apply{
            text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }

        //region
        findViewById<TextView>(R.id.countryRegion).apply{
            text = "Region: ${country.region}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour3, "color", context.packageName)))
        }

        //population
        findViewById<TextView>(R.id.countryPopulation).apply{
            text = "Population: ${country.population}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour3, "color", context.packageName)))
        }

        //language
        findViewById<TextView>(R.id.countryLanguage).apply{
            text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour4, "color", context.packageName)))
        }

        //currency
        findViewById<TextView>(R.id.countryCurrency).apply{
            text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }

        //continent
        findViewById<TextView>(R.id.countryContinent).apply{
            text = "Continent: ${country.continents.joinToString(", ")}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour2, "color", context.packageName)))
        }


        //land area
        findViewById<TextView>(R.id.countryLandArea).apply{
            text = "Land area: ${country.area} km²"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour4, "color", context.packageName)))
        }


        //driving side
        val drivingSide = country.car?.side
        val capitalizedDrivingSide = drivingSide?.capitalize(Locale.getDefault())  // Capitalize first letter

        findViewById<TextView>(R.id.countryDrivingSide).apply{
            text = "Drives on: ${capitalizedDrivingSide ?: "Unknown"}"
            setTextColor(ContextCompat.getColor(context, resources.getIdentifier(colour1, "color", context.packageName)))
        }
    }
}
