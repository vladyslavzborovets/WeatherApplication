package com.example.myweatherapplication.utils

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myweatherapplication.ui.LocationPermissionListener

/**
 * LocationPermision Helper class
 */
class LocationPermissionHelper(private val activity: AppCompatActivity,    private val permissionListener: LocationPermissionListener?) {
    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    fun checkAndRequestLocationPermission() {
        if (isLocationPermissionGranted()) {
            // Permission already granted, perform action
            performLocationAction()
        } else {
            // Request location permission from the user
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    fun handlePermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, perform action
                performLocationAction()
            } else {
                permissionListener?.onPermissionDenied()
            }
        }
    }

    private fun performLocationAction() {
        // Perform the desired action with location access here
        permissionListener?.onPermissionGranted()
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
