package com.example.unitconverter.api.data

data class ExchangeRateData(
    val timestamp : Int,
    val base : String,
    val rates : RatesData
)