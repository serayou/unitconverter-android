package com.example.unitconverter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.example.unitconverter.api.ExchangeRateApi
import com.example.unitconverter.api.data.ExchangeRateData
import com.example.unitconverter.utils.Constants
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    private var fusedLocationClient :FusedLocationProviderClient? = null

    private var tabLayout : TabLayout? = null
    private var viewPager : ViewPager? = null
    private var tvCurrentLocation : TextView? = null
    private var tvCurrentTemperature : TextView? = null
    private var tvCurrentExchangeRate : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initResource()
        setTab()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(checkPermission()){
            requestCurrentLocation()
        }
        requestCurrentExchangeRate()
    }

    private fun initResource(){
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tvCurrentLocation = findViewById(R.id.tv_current_location)
        tvCurrentTemperature = findViewById(R.id.tv_current_temperature)
        tvCurrentExchangeRate = findViewById(R.id.tv_exchange_rate)
    }

    private fun setTab(){
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.unit_converter))
        tabLayout!!.addTab(tabLayout!!.newTab().setText(R.string.tip_calculator))

        val viewPageAdapter = ViewPageAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = viewPageAdapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager!!.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun checkPermission() : Boolean{
        val fineLocationPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)

        return if(fineLocationPermission != PackageManager.PERMISSION_GRANTED ||
            coarseLocationPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            false
        }else{
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestCurrentLocation()
            } else {
                Toast.makeText(this, "위치 권한을 허용해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient?.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken(){
            override fun isCancellationRequested(): Boolean {
                return false
            }

            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                return CancellationTokenSource().token
            }
        })?.addOnSuccessListener { location : Location? ->
            location?.let { location ->
                tvCurrentLocation?.text = getLocalityAddress(location)
            }
        }
    }

    //위치 정보를 정기 업데이트
    private fun requestLocationUpdate(){
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.lastLocation?.let { location ->
                        val latitude = location.latitude
                        val longitude = location.longitude
                    }
                }
            },
            null
        )
    }

    private fun getLocalityAddress(location : Location?) : String?{
        return try {
            Geocoder(this, Locale.US).getFromLocation(location!!.latitude, location!!.longitude, 1)!!.first().run {
                locality
            }
        } catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    private fun requestCurrentExchangeRate(){
        tvCurrentExchangeRate?.text = null

        val exchangeRateApi = ExchangeRateApi.create()
        exchangeRateApi.getExchangeRate(Constants.EXCHANGE_RATE_API_KEY, "KRW").enqueue(object : Callback<ExchangeRateData>{
            override fun onResponse(
                call: Call<ExchangeRateData>,
                response: Response<ExchangeRateData>
            ) {
                val exchangeRateResponse = response.body()
                tvCurrentExchangeRate?.text = (exchangeRateResponse?.rates?.KRW ?: 0.0).toInt().toString() + " " + getString(R.string.currency_name_won)
            }

            override fun onFailure(call: Call<ExchangeRateData>, t: Throwable) {
                call.cancel()
            }
        })
    }
}