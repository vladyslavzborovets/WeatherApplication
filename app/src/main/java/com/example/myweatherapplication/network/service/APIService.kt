package com.example.myweatherapplication.network.service

import com.example.myweatherapplication.network.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  We create one interface class which will be used for declaration of all API calling functions.
 */
interface APIService {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>
}