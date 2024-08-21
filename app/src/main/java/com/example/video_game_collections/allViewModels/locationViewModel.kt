package com.example.video_game_collections.allViewModels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.ui.text.font.FontVariation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.video_game_collections.Screens.permissionDeniedScreen
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient

class locationViewModel: ViewModel() {

    private var _locationSettingsState = MutableLiveData<Boolean>(false)
    var locationSettingsState : LiveData<Boolean> = _locationSettingsState

    private var _locationPermissionState = MutableLiveData<Boolean>(false)
    var locationPermissionState : LiveData<Boolean> = _locationPermissionState


    fun checkLocationSettings(
        context: Context,
        settingsLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
        onSuccess: () -> Unit,
    ){
        //settingsLauncher -> it is the rememberLauncherForActivityResult which expects a result(allow or deny)


        //create my own request (this is done to match the app settings with my requirement)
        var myLocationRequest = LocationRequest.create()

        //build an LocationSettingsRequest object using myLocaitonRequest since LocationSettingsRequest object is required to checkLocationSettings
        var myLocationRequestBuilt = LocationSettingsRequest.
        Builder().
        addLocationRequest(myLocationRequest).build()

        //get Location settings from settings page in our phone
        var mySettingsClient = LocationServices.getSettingsClient(context)

        //pass our built object in checkLocationSettings()
        // this will check the app's settings against our request
        mySettingsClient.checkLocationSettings(myLocationRequestBuilt)
            .addOnSuccessListener {

                _locationSettingsState.value = true
                Log.i("locationresponse","settings already all Okay")
                onSuccess()



            }
            .addOnFailureListener {

                //if requested setting is not matched then prompt the user to enable location in settings
                // ResolvableApiException -> can be solved using user input
                if(it is ResolvableApiException){
                    var myIntentSender = IntentSenderRequest.Builder(it.resolution).build()

                    settingsLauncher.launch(myIntentSender)
                }


            }




    }


    fun checkLocationPermission(
        context: Context,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        onDenied: () -> Unit

    ){

      //  permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        if(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION

        ) == PackageManager.PERMISSION_GRANTED){
            Log.i("locationresponse","location permission  already granted ")
            _locationPermissionState.value = true
           // onDenied()

        }else{
            if ((context as Activity).shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show rationale to the user, then request permission
                Toast.makeText(context,"show rationale",Toast.LENGTH_LONG).show()

                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

            } else {
                Toast.makeText(context,"pern deneid peram",Toast.LENGTH_LONG).show()
                _locationPermissionState.value = false

                onDenied()

                // Permission was denied permanently, show a dialog to guide the user to settings


            }
        }



    }



    fun makelocationSettingsStateTrue(){
        _locationSettingsState.value = true
    }
    fun makelocationSettingsStateFalse(){
        _locationSettingsState.value = false
    }

    fun makelocationPermissionStateTrue(){
        _locationPermissionState.value = true
    }
    fun makelocationPermissionStateFalse(){
        _locationPermissionState.value = false
    }


    fun getLastKnownLocation(context: Context, onLocationReceived: (Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Log.i("locationresponse", "inside getLastKnownLocation")

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000 // Desired interval in milliseconds
                fastestInterval = 5000 // Fastest interval in milliseconds
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // High accuracy
            }

            // Create a location callback to receive location updates
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        // Pass the location to the provided callback function
                        onLocationReceived(location)
                        // Optionally, stop location updates if you only need one update
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }

            // Request location updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()

            )

        }
        else{
            Log.i("locationresponse","oombe")
        }


    }



}



