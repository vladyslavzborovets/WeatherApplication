package com.example.myweatherapplication.ui

/**
 * Location permission listener for Location Permission Granted or Denied Status
 */
interface LocationPermissionListener {
    fun onPermissionDenied()
    fun onPermissionGranted()
}
