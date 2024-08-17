package com.example.video_game_collections.helperFunctions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.video_game_collections.Screens.permissionDeniedPage
import com.example.video_game_collections.allViewModels.locationViewModel

@Composable
fun locationSettingsLauncherFunction(
    context : Context,
    locationViewModel: locationViewModel,
    navController: NavController
): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {

    var permissionLauncher = locationPermissionLauncher(navController)

    //launcher which is called to allow/deny settings' location
    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK) {

            locationViewModel.checkLocationPermission(context,permissionLauncher){
                    navController.navigate(permissionDeniedPage)
            }
            // User agreed to make required location settings changes
            Log.i("locationresponse", "User enabled location settings.")




        } else {
            // User didn't make required location settings changes
            navController.navigate(permissionDeniedPage)
            Log.i("locationresponse", "User did not enable location settings.")
        }



    }


    return locationSettingsLauncher
}

@Composable
fun locationPermissionLauncher(
    navController: NavController
): ManagedActivityResultLauncher<String, Boolean> {

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {isGranted ->

        if(isGranted){
            Log.i("locationresponse","location permission granted")
        }else{
            Log.i("locationresponse","location permission dddddenied")
            navController.navigate(permissionDeniedPage)

        }

    }

    return locationPermissionLauncher

}

