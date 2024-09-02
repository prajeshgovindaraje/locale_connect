package com.example.video_game_collections.Screens.CustomerScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.Screens.AutoResizedText
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.Screens.NavigationPages.allProductsForCustomerByThisSellerPage
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.loginStatus
import com.example.video_game_collections.dataModels.shopCardModel
import com.example.video_game_collections.helperFunctions.locationPermissionLauncher
import com.example.video_game_collections.helperFunctions.locationSettingsLauncherFunction

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cutomerScreen(
    modifier: Modifier = Modifier,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel,
    locationViewModel : locationViewModel,
    viewModel: UI_ViewModel
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


    //Functions call before launching Customer Screen

    LaunchedEffect(null) {





    }
    //----------------location
    LaunchedEffect(
        key1 = observedLoginStatus.value,
        key2 = obserevedLocationPermissionState.value,
        key3 = obseveredLocationSettingsState.value
    ) {
        Log.i("locationresponse", "inside customer launch")

        when (observedLoginStatus.value) {
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {


                //location
                if (obserevedLocationPermissionState.value == true && obseveredLocationSettingsState.value == true) {
                    Log.i("locationresponse", "we can get location now")
                    locationViewModel.getLastKnownLocation(context) {

                        Log.i("locationresponse", "the got location is "+it.latitude.toString() + "" + it.longitude.toString())
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


                //location
                locationViewModel.checkLocationSettings(context, settingsLauncher) {
                    locationViewModel.checkLocationPermission(context, permissionLauncher) {
                        Log.i("locationresponse", obserevedLocationPermissionState.value.toString())

                        if (obserevedLocationPermissionState.value == false) {
                            navController.navigate(NavigationPages.permissionDeniedPage)
                        }


                    }

                }


//                fireBaseAuthViewModel.auth.currentUser?.let {
//                    fireStoreViewModel.displayAllProductsforCustomer(
//                        it.uid)
//                }


            }

            is loginStatus.LoggedOut -> {

                navController.navigate(NavigationPages.loginPage)

            }

            else -> Unit
        }

    }


    //UI for Customer Screen
    val switchSap by viewModel.switchSap.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)),
                title = {
                    Text(text = "Customer Screen", style = MaterialTheme.typography.headlineMedium)
                        },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary),

            )

                 },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerpadding ->
        if (obserevedAllShopsForCustomersState.value.isEmpty()) {

                Box (modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center

                ){
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
        }


        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                SwitchSAP( viewModel = viewModel)
                Spacer(modifier = Modifier.height(15.dp))


                if (!switchSap) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),

                    ) {
                            items(obserevedAllShopsForCustomersState.value) { shop ->
                                ShopCard(
                                    shop = shop,
                                    navController = navController,
                                    fireStoreViewModel = fireStoreViewModel,
                                )
                            }
                    }
                }
                else{
                    allProductsForCustomer(
                        fireBaseAuthViewModel = fireBaseAuthViewModel,
                        navController = navController,
                        fireStoreViewModel = fireStoreViewModel,
                        locationViewModel = locationViewModel
                    )
                }


            }
        }


    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopCard(shop: shopCardModel, navController: NavController, fireStoreViewModel: fireStoreViewModel) {

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 8.dp,
            focusedElevation = 15.dp,
        ),
        onClick = {
            fireStoreViewModel.displayAllProductsBySeller(shop.userID)
            navController.navigate(allProductsForCustomerByThisSellerPage)
        },
        modifier = Modifier
            .height(250.dp)
            .padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

                AsyncImage(
                    model = shop.shopImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                    ,
                    contentScale = ContentScale.Crop,
                )



            Box (
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(60.dp)
                    .offset(y = 75.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        RoundedCornerShape(20.dp)
                    )

                ,
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AutoResizedText(
                        text = shop.shopName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = shop.shopType,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SwitchSAP(viewModel: UI_ViewModel) {
    val switchSap by viewModel.switchSap.collectAsState(false)
    val selectedColor = MaterialTheme.colorScheme.onSecondaryContainer
    val unselectedColor = MaterialTheme.colorScheme.tertiaryContainer
    


    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 80.dp)
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = CircleShape)
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp))
                .clickable {
                    viewModel.changeSap(false)
                }
                .background(if (!switchSap) selectedColor else unselectedColor)
            ,
        ){
            Text(text = "Shops",
                color =  if (!switchSap) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                .clickable {
                    viewModel.changeSap(true)
                }
                .background(if (switchSap) selectedColor else unselectedColor)
            ,
        ){
            Text(text = "Products",
                color =if (switchSap) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    // Animated content based on the selected tab
    AnimatedContent(
        targetState = switchSap,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn() with
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() using
                    SizeTransform(clip = false)
        }
    ) { targetState ->
        if (targetState) {
            ProductsContent()
        } else {
            ShopsContent()
        }
    }
}

@Composable
fun ShopsContent() {
    // Add content for Shops here
    Column {
        Text(text = "Shops Content", modifier = Modifier.padding(16.dp))
        // More UI elements
    }
}

@Composable
fun ProductsContent() {
    // Add content for Products here
    Column {
        Text(text = "Products Content", modifier = Modifier.padding(16.dp))
        // More UI elements
    }
}




