package com.example.video_game_collections.Screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest

@Composable
fun permissionDeniedScreen(
    locationViewModel: locationViewModel,
    navController: NavController,
    authViewModel: fireBaseAuthViewModel
) {

    var context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    Log.i("locationresponse","page permssion denied")

    var observeLocationSettingsState = locationViewModel.locationSettingsState.observeAsState()
    var observeLocationPermissionState = locationViewModel.locationPermissionState.observeAsState()

    var showDialog by remember { mutableStateOf(true) }


    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {

                Log.i("locationresponse", "on resume activity permission denied")
                super.onResume(owner)

                var myLocationRequest = LocationRequest.create()
                var myLocationRequestBuilt = LocationSettingsRequest.Builder()
                    .addLocationRequest(myLocationRequest)
                    .build()
                var mySettingsClient = LocationServices.getSettingsClient(context)

                var settingsCheckCompleted = false
                var permissionCheckCompleted = false

                fun updateDialogVisibility() {
                    if (permissionCheckCompleted && settingsCheckCompleted) {

                        val permissionEnabled = observeLocationPermissionState.value

                        val settingsEnabled = observeLocationSettingsState.value

                        if(permissionEnabled == false || settingsEnabled == false){
                            showDialog = true
                        }else if(permissionEnabled == true && settingsEnabled == true){
                            showDialog = false
                        }

                        Log.i("locationresponse", "Dialog visibility updated: $showDialog")
                    }
                }

                mySettingsClient.checkLocationSettings(myLocationRequestBuilt)
                    .addOnSuccessListener {
                        locationViewModel.makelocationSettingsStateTrue()
                        settingsCheckCompleted = true
                        updateDialogVisibility()
                    }
                    .addOnFailureListener {
                        locationViewModel.makelocationSettingsStateFalse()
                        settingsCheckCompleted = true
                        updateDialogVisibility()
                    }

                val permissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (permissionGranted) {
                    locationViewModel.makelocationPermissionStateTrue()
                } else {
                    locationViewModel.makelocationPermissionStateFalse()
                }

                permissionCheckCompleted = true
                updateDialogVisibility()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    if(showDialog){
       AlertDialog(
           onDismissRequest = { /* Handle dismiss */ },
           title = { Text("Permission Required") },
           text = { Text("Please grant location permission from settings.") },
           confirmButton = {
               Button(onClick = {
                   // Navigate to app settings
                   val intent = Intent(
                       Settings.ACTION_LOCATION_SOURCE_SETTINGS
                   )
                   context.startActivity(intent)
               }) {
                   Text("Open Settings")
               }
           }
       )
   }else{
       authViewModel.getUserRole {role->

           if(role == "seller"){
               navController.navigate(NavigationPages.sellerPage)
           }else{
               navController.navigate(NavigationPages.customerPage)
           }

       }
   }


}