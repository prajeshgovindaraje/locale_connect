package com.example.video_game_collections.Screens

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.loginStatus
import com.example.video_game_collections.dataModels.productModel
import com.example.video_game_collections.dataModels.shopCardModel
import com.example.video_game_collections.helperFunctions.locationPermissionLauncher
import com.example.video_game_collections.helperFunctions.locationSettingsLauncherFunction
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cutomerScreen(
    modifier: Modifier = Modifier,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel,
    locationViewModel : locationViewModel
) {


    val obseveredLocationSettingsState = locationViewModel.locationSettingsState.observeAsState()
    val obserevedLocationPermissionState =
        locationViewModel.locationPermissionState.observeAsState()

    val obserevedAllShopsForCustomersState =
        fireStoreViewModel.allShopsForCustomerState.observeAsState(
            emptyList<shopCardModel>()
        )


    val observedLoginStatus = fireBaseAuthViewModel.loginStatusState.observeAsState()
    var context = LocalContext.current


    //location---------------

    //creating a launcher for location settings
    var settingsLauncher =
        locationSettingsLauncherFunction(context, locationViewModel, navController)


    //creating a launcher for location permission
    var permissionLauncher = locationPermissionLauncher(navController)

    LaunchedEffect(null) {
        fireBaseAuthViewModel.auth.currentUser?.let {
            fireStoreViewModel.displayAllShopsForCustomer(
                it.uid)
        }
    }
    //----------------location
    LaunchedEffect(
        key1 = observedLoginStatus.value,
        obserevedLocationPermissionState.value,
        obseveredLocationSettingsState.value
    ) {
        Log.i("locationresponse", "inside customer launch")

        when (observedLoginStatus.value) {
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {

                if (obserevedLocationPermissionState.value == true && obseveredLocationSettingsState.value == true) {
                    Log.i("getlocation", "we can get ra")
                    locationViewModel.getLastKnownLocation(context) {

                        Log.i("getlocation", it.latitude.toString() + "" + it.longitude.toString())
                        var myLocation = listOf(it.latitude, it.longitude)
                        var userId = fireBaseAuthViewModel.auth.currentUser?.uid

                        if (userId != null) {
                            fireStoreViewModel.updateUserModelWithlocation(
                                userId,
                                myLocation
                            )
                        }

                    }


                }


                locationViewModel.checkLocationSettings(context, settingsLauncher) {
                    locationViewModel.checkLocationPermission(context, permissionLauncher) {
                        Log.i("locationresponse", obserevedLocationPermissionState.value.toString())

                        if (obserevedLocationPermissionState.value == false) {
                            navController.navigate(permissionDeniedPage)
                        }


                    }

                }


//                fireBaseAuthViewModel.auth.currentUser?.let {
//                    fireStoreViewModel.displayAllProductsforCustomer(
//                        it.uid)
//                }


            }

            is loginStatus.LoggedOut -> {

                navController.navigate(loginPage)

            }

            else -> Unit
        }

    }



    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Customer Screen",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(obserevedAllShopsForCustomersState.value) {


                    Card(
                        onClick = {

                                fireStoreViewModel.displayAllProductsBySeller(it.userID)

                                  navController.navigate(allProductsBySellerPage)

                        },
                        modifier = Modifier.height(250.dp).padding(10.dp),

                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = it.shopImage,
                                contentDescription =null,
                                modifier = Modifier.fillMaxHeight(0.7f).fillMaxWidth(0.9f).border(2.dp, Color.Green),
                                contentScale = ContentScale.Crop
                            )

                                Text(text = it.shopName)
                                Text(text = it.shopType)

                        }

                    }
                }


            }

            Button(onClick = {
                navController.navigate(allProductsForCustomerPage)

            }) {

                Text(text = "click me view all available products")

            }


            TextButton(onClick = {
                fireBaseAuthViewModel.signOut()
            }) {
                Text(text = "SIGN OUT")
            }


        }
    }
}








