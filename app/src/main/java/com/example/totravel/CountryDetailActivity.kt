package com.example.totravel

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class CountryDetailActivity : AppCompatActivity() {
    private lateinit var countryName: TextView
    private lateinit var countryCapital: TextView
    private lateinit var countryRegion: TextView
    private lateinit var countryPopulation: TextView
    private lateinit var countryFlag: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        countryName = findViewById(R.id.countryName)
        countryCapital = findViewById(R.id.countryCapital)
        countryRegion = findViewById(R.id.countryRegion)
        countryPopulation = findViewById(R.id.countryPopulation)
        countryFlag = findViewById(R.id.countryFlag)

        val country = intent.getParcelableExtra<Country>("country")
        country?.let {
            countryName.text = it.name
            countryCapital.text = it.capital ?: "No capital"
            countryRegion.text = it.region
            countryPopulation.text = it.population.toString()
            Glide.with(this).load(it.flags.png).into(countryFlag)
        }
    }
}
