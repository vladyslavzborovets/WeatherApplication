package com.example.myweatherapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myweatherapplication.databinding.ActivityMainBinding
import com.example.myweatherapplication.ui.LocationPermissionListener
import com.example.myweatherapplication.ui.viewModels.WeatherViewModel
import com.example.myweatherapplication.utils.Const.Companion.EMPTY_STRING
import com.example.myweatherapplication.utils.Const.Companion.ICON_BASE_URL
import com.example.myweatherapplication.utils.Const.Companion.SHARED_PREF_CITY_KEY
import com.example.myweatherapplication.utils.LocationPermissionHelper
import com.example.myweatherapplication.utils.SharedPreferencesUtil
import com.example.myweatherapplication.utils.ToastUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationPermissionListener {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        locationPermissionHelper = LocationPermissionHelper(this, this)
        locationPermissionHelper.checkAndRequestLocationPermission()

        setUpViewModel()

        weatherViewModel.isWeatherDataAvailable.observe(this) {
            if (it) {
                activityMainBinding.weatherDataAvailable.visibility=View.VISIBLE
                activityMainBinding.weatherDataNotAvailable.visibility=View.GONE
            } else {
                activityMainBinding.weatherDataAvailable.visibility=View.GONE
                activityMainBinding.weatherDataNotAvailable.visibility=View.VISIBLE
            }
        }

        weatherViewModel.isShowProgress.observe(this, Observer {
            if (it){
                activityMainBinding.mainProgressBar.visibility = View.VISIBLE
            }
            else{
                activityMainBinding.mainProgressBar.visibility = View.GONE
            }
        })

        weatherViewModel.errorMessage.observe(this, Observer {
            ToastUtil.showShortToast(this, it)
        })

        activityMainBinding.theSearchButton.setOnClickListener { it->
            if(activityMainBinding.theSearchBar.text.isEmpty()){
                ToastUtil.showShortToast(this, "Please Enter valid city name!")
            }
            else{
                val searchText = activityMainBinding.theSearchBar.text
                weatherViewModel.getWeatherFromAPI(searchText.toString())
            }
        }

        weatherViewModel.responseContainer.observe(this, Observer {
            it?.let{
                setLabelText(activityMainBinding.labelLatitude, "Latitude : ${it.coord?.lat}")
                setLabelText(activityMainBinding.labelLongitude, "Longitude : ${it.coord?.lon}")
                setLabelText(activityMainBinding.labelWeather, "Weather : ${it.weather?.getOrNull(0)?.description}")
                setLabelText(activityMainBinding.labelWindSpeed, "Wind Speed : ${it.wind?.speed}")
                setLabelText(activityMainBinding.labelWindDegree, "Wind Degree : ${it.wind?.deg}")
                setLabelText(activityMainBinding.labelWindTemperature, "Temperature : ${it.main?.temp}")
                setLabelText(activityMainBinding.labelWindPressure, "Pressure : ${it.main?.pressure}")
                setLabelText(activityMainBinding.labelWindHumidity, "Humidity : ${it.main?.humidity}")
                Glide.with(this)
                    .load(ICON_BASE_URL+ (it.weather?.get(0)?.icon) +".png")
                    .placeholder(androidx.constraintlayout.widget.R.drawable.abc_ic_clear_material)
                    .into(activityMainBinding.imageWeather)
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val sharedPreferencesUtil = SharedPreferencesUtil.getInstance(this)
        sharedPreferencesUtil.putString(SHARED_PREF_CITY_KEY, activityMainBinding.theSearchBar.text.toString() )
    }

    private fun setUpViewModel(){
        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        loadLastEnteredCity()
    }

    fun setLabelText(label: TextView, value: String?) {
        label.text = value ?: EMPTY_STRING
    }

    private fun loadLastEnteredCity() {
        val sharedPreferencesUtil = SharedPreferencesUtil.getInstance(this)
        val myLastEnteredCity = sharedPreferencesUtil.getString(SHARED_PREF_CITY_KEY)
        if (myLastEnteredCity != null) {
            activityMainBinding.theSearchBar.text = Editable.Factory.getInstance().newEditable(myLastEnteredCity)
            weatherViewModel.getWeatherFromAPI(myLastEnteredCity)
        } else {
            activityMainBinding.theSearchBar.text = Editable.Factory.getInstance().newEditable("")
            ToastUtil.showShortToast(this, "Please enter US city name to search its weather")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.handlePermissionsResult(requestCode, grantResults)
    }

    override fun onPermissionDenied() {
        ToastUtil.showShortToast(this, "Location Permission Denied")
        val sharedPreferencesUtil = SharedPreferencesUtil.getInstance(this)
        sharedPreferencesUtil.putString(SHARED_PREF_CITY_KEY, "" )
        loadLastEnteredCity()
    }

    override fun onPermissionGranted() {
        ToastUtil.showShortToast(this, "Location Permission is granted!")
    }
}