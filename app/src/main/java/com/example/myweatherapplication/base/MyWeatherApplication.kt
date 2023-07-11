package com.example.myweatherapplication.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class is a base class for maintaining global application state
 */
@HiltAndroidApp
class MyWeatherApplication : Application() {

}