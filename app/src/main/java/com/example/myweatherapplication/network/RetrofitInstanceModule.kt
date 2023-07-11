package com.example.myweatherapplication.network

import com.example.myweatherapplication.network.service.APIService
import com.example.myweatherapplication.utils.Const.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * In this theRetrofitInstance() function, we build the retrofit instance for generating API calling.
 * We have to pass the base url of the API.
 */
@Module
@InstallIn(SingletonComponent::class)
class RetrofitInstanceModule {
    @Provides
    @Singleton
    fun theRetrofitInstance(): APIService {
        val API: APIService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIService::class.java)
        }
        return API
    }
}