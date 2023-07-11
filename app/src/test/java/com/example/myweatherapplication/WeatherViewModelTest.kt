package com.example.myweatherapplication

import com.example.myweatherapplication.network.model.Clouds
import com.example.myweatherapplication.network.model.Coord
import com.example.myweatherapplication.network.model.Main
import com.example.myweatherapplication.network.model.Sys
import com.example.myweatherapplication.network.model.Weather
import com.example.myweatherapplication.network.model.WeatherResponse
import com.example.myweatherapplication.network.model.Wind
import com.example.myweatherapplication.network.service.APIService
import com.example.myweatherapplication.ui.viewModels.WeatherViewModel
import com.example.myweatherapplication.utils.Const.Companion.API_KEY
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response


class WeatherViewModelTest {

    @Mock
    private lateinit var retrofitInstance: APIService

    private lateinit var weatherViewModel: WeatherViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        weatherViewModel = WeatherViewModel(retrofitInstance)
    }

    @Test
    suspend fun testWeatherAPI_Success() {
        // Create a mock weather response object
        val weatherResponse = WeatherResponse(
            Coord(-87.65, 41.85),
            listOf(Weather(802, "Clouds", "scattered clouds", "03d")),
            "stations",
            Main(290.41, 289.13, 288.2, 292.35, 1020, 36),
            10000,
            Wind(1.34, 187),
            Clouds(26),
            1684425271,
            Sys(2, 2005153, "US", 1684405680, 1684458377),
            -18000,
            4887398,
            "Chicago",
            200, "Success Response"
        )

        // Convert the mock weather response object to JSON string
        val jsonWeatherResponse = Gson().toJson(weatherResponse)

        // Mock the API service response
        `when`(retrofitInstance.getWeatherByCity("Chicago", API_KEY))
            .thenReturn(
                Response.success(weatherResponse)
            )

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("Chicago")

        // Verify the expected behavior or assertions
        assert(weatherViewModel.responseContainer.value == weatherResponse)
        assert(weatherViewModel.isShowProgress.value == false)
        assert(weatherViewModel.isWeatherDataAvailable.value == true)
        assert(weatherViewModel.errorMessage.value == null)
    }

    @Test
    suspend fun testWeatherAPI_Error() {
        val errorMessage = "Error: City not found"

        // Mock the API service response with an error
        `when`(retrofitInstance.getWeatherByCity("InvalidCity", API_KEY))
            .thenReturn(
                Response.error(404, ResponseBody.create("application/json".toMediaTypeOrNull(), errorMessage))
            )

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("InvalidCity")

        // Verify the expected behavior or assertions
        assert(weatherViewModel.responseContainer.value == null)
        assert(weatherViewModel.isShowProgress.value == false)
        assert(weatherViewModel.isWeatherDataAvailable.value == false)
        assert(weatherViewModel.errorMessage.value == errorMessage)
    }

    @Test(expected = Exception::class)
    suspend fun testWeatherAPI_Exception() {
        // Mock the API service response with an exception
        `when`(retrofitInstance.getWeatherByCity("ExceptionCity", API_KEY))
            .thenThrow(Exception("Network Exception"))

        // Call the getWeatherFromAPI method
        weatherViewModel.getWeatherFromAPI("ExceptionCity")

        // Verify the expected behavior or assertions
        // Exception is expected to be thrown
    }
}