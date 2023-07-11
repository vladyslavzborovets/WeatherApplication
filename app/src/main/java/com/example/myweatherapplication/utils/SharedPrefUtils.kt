package com.example.myweatherapplication.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
 * Shared Preference Helper Class
 */
class SharedPreferencesUtil private constructor(private val context: Context) {

    companion object {
        private const val PREF_NAME = "CityPreferences"
        private const val DEFAULT_STRING_VALUE = "Chicago"

        @Volatile
        private var instance: SharedPreferencesUtil? = null

        fun getInstance(context: Context): SharedPreferencesUtil {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesUtil(context).also { instance = it }
            }
        }
    }

    var sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, DEFAULT_STRING_VALUE) ?: DEFAULT_STRING_VALUE
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}

private operator fun Any.setValue(
    sharedPreferencesUtil: SharedPreferencesUtil,
    property: KProperty<*>,
    sharedPreferences: SharedPreferences
) {
    TODO("Not yet implemented")
}
