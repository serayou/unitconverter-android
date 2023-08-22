package com.example.unitconverter.api

import com.example.unitconverter.api.data.ExchangeRateData
import com.example.unitconverter.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest.json")
    fun getExchangeRate(
        @Query("app_id") appId : String,
        @Query("symbols") symbols : String
    ) : Call<ExchangeRateData>

    companion object {
        fun create(): ExchangeRateApi {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constants.EXCHANGE_RATE_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExchangeRateApi::class.java)
        }
    }
}