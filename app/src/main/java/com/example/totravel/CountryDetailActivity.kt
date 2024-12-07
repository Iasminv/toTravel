package com.example.totravel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.totravel.network.Country

class CountryDetailActivity : AppCompatActivity() {
    private lateinit var countryName: TextView
    private lateinit var countryOfficialName: TextView
    private lateinit var countryCapital: TextView
    private lateinit var countryRegion: TextView
    private lateinit var countryPopulation: TextView
    private lateinit var countryFlag: ImageView
    private lateinit var countryLanguage: TextView
    private lateinit var countryCurrency: TextView
    private lateinit var countryContinent: TextView
    private lateinit var countryLandArea: TextView
    private lateinit var countryDrivingSide: TextView
    private lateinit var countryMap: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        countryName = findViewById(R.id.countryName)
        countryOfficialName = findViewById(R.id.countryOfficialName)
        countryCapital = findViewById(R.id.countryCapital)
        countryRegion = findViewById(R.id.countryRegion)
        countryPopulation = findViewById(R.id.countryPopulation)
        countryFlag = findViewById(R.id.countryFlag)
        countryLanguage = findViewById(R.id.countryLanguage)
        countryCurrency = findViewById(R.id.countryCurrency)
        countryContinent = findViewById(R.id.countryContinent)
        countryLandArea = findViewById(R.id.countryLandArea)
        countryDrivingSide = findViewById(R.id.countryDrivingSide)
        countryMap = findViewById(R.id.countryMap)

        val country = intent.getParcelableExtra<Country>("country")
        country?.let { country ->
            countryName.text = country.name.common
            countryOfficialName.text = "Official Name: ${country.name.official}"
            countryCapital.text = "Capital: ${country.capital?.joinToString(", ") ?: "No capital"}"
            countryRegion.text = "Region: ${country.region}"
            countryPopulation.text = "Population: ${country.population}"
            countryLanguage.text = "Language: ${country.languages?.values?.joinToString(", ") ?: "Not specified"}"
            countryCurrency.text = "Currency: ${country.currencies?.values?.joinToString(", ") { currency -> "${currency.name} (${currency.symbol})" } ?: "Not specified"}"
            countryContinent.text = "Continent: ${country.continents.joinToString(", ")}"
            countryLandArea.text = "Land Area: ${country.area} km²"
            countryDrivingSide.text = "Driving Side: ${country.demonyms.eng.m}"
            Glide.with(this).load(country.flags.png).into(countryFlag)
            countryMap.setOnClickListener {
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(country.maps.googleMaps))
                startActivity(mapIntent)
            }
        }
    }
}
