package com.example.myweatherapplication

import com.example.myweatherapplication.network.model.Clouds
import com.example.myweatherapplication.network.model.Coord
import com.example.myweatherapplication.network.model.Main
import com.example.myweatherapplication.network.model.Sys
import com.example.myweatherapplication.network.model.Weather
import com.example.myweatherapplication.network.model.WeatherResponse
import com.example.myweatherapplication.network.model.Wind
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherResponseTest {

    @Test
    fun weatherResponseToReturnCorrectValues() {
        // Create sample data for testing
        val coord = Coord(10.0, 20.0)
        val weather = listOf(Weather(1, "Cloudy", "Cloudy weather", "cloud"))
        val base = "base"
        val main = Main(25.0, 24.0, 20.0, 30.0, 1012, 70)
        val visibility = 1000
        val wind = Wind(10.0, 180)
        val clouds = Clouds(75)
        val dt = 1635451200L
        val sys = Sys(1, 123, "US", 1635424851L, 1635464281L)
        val timezone = 3600
        val id = 12345
        val name = "City"
        val cod = 200
        val message = "Success"

        // Create WeatherResponse object
        val weatherResponse = WeatherResponse(
            coord, weather, base, main, visibility, wind,
            clouds, dt, sys, timezone, id, name, cod, message
        )

        // Verify the values
        assertEquals(coord, weatherResponse.coord)
        assertEquals(weather, weatherResponse.weather)
        assertEquals(base, weatherResponse.base)
        assertEquals(main, weatherResponse.main)
        assertEquals(visibility, weatherResponse.visibility)
        assertEquals(wind, weatherResponse.wind)
        assertEquals(clouds, weatherResponse.clouds)
        assertEquals(dt, weatherResponse.dt)
        assertEquals(sys, weatherResponse.sys)
        assertEquals(timezone, weatherResponse.timezone)
        assertEquals(id, weatherResponse.id)
        assertEquals(name, weatherResponse.name)
        assertEquals(cod, weatherResponse.cod)
        assertEquals(message, weatherResponse.message)
    }
}
