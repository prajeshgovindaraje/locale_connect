package com.example.video_game_collections.Screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.loginStatus
import com.example.video_game_collections.allViewModels.ordersSellerSideViewModel
import com.example.video_game_collections.helperFunctions.generateRandomID
import com.example.video_game_collections.helperFunctions.locationPermissionLauncher
import com.example.video_game_collections.helperFunctions.locationSettingsLauncherFunction
import com.google.android.gms.location.LocationServices



@Composable
fun sellerScreen(
    modifier: Modifier = Modifier,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    locationViewModel: locationViewModel,
    ordersSellerSideViewModel:ordersSellerSideViewModel,
    imageViewModel: imageViewModel
) {

    val observedAddProductDialogueCardState = ui_viewModel.addProductDialogueCardState.observeAsState(initial = false)
    val observedLoginStatus  = fireBaseAuthViewModel.loginStatusState.observeAsState()

    val sellerID = fireBaseAuthViewModel.auth.currentUser?.uid
    val context = LocalContext.current

    val obseveredLocationSettingsState = locationViewModel.locationSettingsState.observeAsState()
    val obserevedLocationPermissionState = locationViewModel.locationPermissionState.observeAsState()
    //creating a launcher for location settings
    var settingsLauncher = locationSettingsLauncherFunction(context,locationViewModel,navController)


    //creating a launcher for location permission
    var permissionLauncher = locationPermissionLauncher(navController)

    //ID for each product
    val pID = generateRandomID().generateRandomAlphanumericString(21)



    LaunchedEffect(
        key1 = observedLoginStatus.value,
        obseveredLocationSettingsState.value,
        obserevedLocationPermissionState.value
    ) {
        when (observedLoginStatus.value) {
            is loginStatus.LoggedIn -> {
                Log.i("response", "Checking Location Permission and Services")
                if(obserevedLocationPermissionState.value == true && obseveredLocationSettingsState.value == true){
                    Log.i("getlocation","we can get ra")
                    locationViewModel.getLastKnownLocation(context){

                        Log.i("getlocation",it.latitude.toString()+""+it.longitude.toString())
                        var myLocation = listOf(it.latitude,it.longitude)
                        var userId = fireBaseAuthViewModel.auth.currentUser?.uid

                        if (userId != null) {
                            fireStoreViewModel.updateUserModelWithlocation(
                                userId,
                                myLocation
                            )
                        }

                    }


                }


                locationViewModel.checkLocationSettings(context, settingsLauncher){
                    locationViewModel.checkLocationPermission(context,permissionLauncher){
                        Log.i("locationresponse",obserevedLocationPermissionState.value.toString())

                        if(obserevedLocationPermissionState.value == false){
                            navController.navigate(NavigationPages.permissionDeniedPage)
                        }


                    }

                }


            }
            is loginStatus.LoggedOut -> {
                navController.navigate(NavigationPages.loginPage)
            }
            else -> Unit
        }
    }

    // Function to get the current location and print it




    var selectedURI by remember {
        mutableStateOf<String>("")
    }

    LaunchedEffect(null) {
        fireBaseAuthViewModel.auth.currentUser?.let {
            imageViewModel.getCurrentShopImageURL(
                fireBaseAuthViewModel = fireBaseAuthViewModel,
                userID = it.uid
            ){
                selectedURI = it
            }
        }
    }

    var galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {

            //this check so that when no image is chosen in gallery launcher then the previous one must remain
            //else null will be assigned and image becomes empty
            if(it != null){
                fireBaseAuthViewModel.auth.currentUser?.let { it1 ->
                    imageViewModel.uploadShopImage(
                        fireStoreViewModel,
                        it,
                        it1.uid
                    ){
                        imageViewModel.getCurrentShopImageURL(
                            fireBaseAuthViewModel = fireBaseAuthViewModel,
                            userID = fireBaseAuthViewModel.auth.currentUser!!.uid,

                        ){
                            selectedURI = it
                        }
                    }
                }


            }
        }


    )





    Box(
        modifier = Modifier.fillMaxSize(),
       // contentAlignment = Alignment.Center
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Cyan)

            .height(150.dp)){
            AsyncImage(

                model = selectedURI,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(2.dp, Color.Red)
                    .clickable {

                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                    },
                contentDescription = null,
                contentScale = ContentScale.Crop

                )
        }

        Column(
            modifier =  Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            Text(
                text = "Seller Screen",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(30.dp))


            IconButton(onClick = {

                    ui_viewModel.makeAddProductDialogueCardVisible()

            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                fireBaseAuthViewModel.auth.currentUser?.let {
                    fireStoreViewModel.displayAllProductsBySeller(
                        it.uid)
                }

                navController.navigate(NavigationPages.allProductsBySellerPage)
            }) {
                Text(text = "See All Products")
            }

            Spacer(modifier = Modifier.height(30.dp))


            Button(onClick = {
                if (sellerID != null) {
                    ordersSellerSideViewModel.displayOrderForCurrentSeller(sellerID)
                }
                navController.navigate(NavigationPages.sellerOrderScreenPage)
            }) {

                    Text(text = "Clicke to view Orders")

            }


            Spacer(modifier = Modifier.height(30.dp))


            TextButton(onClick = {
                fireBaseAuthViewModel.signOut()
            }) {
                Text(text = "SIGN OUT")
            }
        }






        if(observedAddProductDialogueCardState.value == true){
            addProductDialogueCard(
                context = context,
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel,
                fireBaseAuthViewModel = fireBaseAuthViewModel,
                imageViewModel = imageViewModel,
                pID = pID
            )
        }
    }

}



@Composable
fun addProductDialogueCard(

    context: Context,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    imageViewModel: imageViewModel,
    pID : String
) {

    var pName by remember {
        mutableStateOf("")
    }
    var pCost by remember {
        mutableStateOf(0.0)
    }

    var selectedURI by remember {
        mutableStateOf<Uri?>(null)
    }

    var galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedURI = it
        }

    )

    Dialog(
        onDismissRequest = { ui_viewModel.makeAddProductDialogueCardHidden() },

        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {


        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxHeight(0.7f)
                .fillMaxWidth(0.9f)
                .border(1.dp, Color.Cyan, RoundedCornerShape(15.dp))
                .verticalScroll(state = rememberScrollState())


        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "ADD YOUR PRODUCT")

                Spacer(modifier = Modifier.height(5.dp))

                OutlinedTextField(
                    value = pName, onValueChange = {

                        pName = it

                    },
                    label = {
                        Text(text = "Product Name")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = pCost.toString(), onValueChange = {

                        if(it.isNotEmpty()){
                            pCost = it.toDouble()

                        }else{

                        }

                    },

                    label = {
                        Text(text = "Product Cost")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
                //image adding------------------>
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center

                    ) {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = selectedURI,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .height(140.dp)
                                    .border(2.dp, Color.Red),
                                contentScale = ContentScale.Crop

                                )


                            Button(onClick = {
                                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }) {
                                Text(text = "ADD IMAGE")
                            }
                        }
                    }
                //<------------------image adding
                Spacer(modifier = Modifier.height(10.dp))

                Row(

                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Button(
                        onClick = {

                            ui_viewModel.makeAddProductDialogueCardHidden()

                        },
                        //modifier=Modifier.weight(1f)
                    ) {
                        Text(text = "BACK")

                    }

                    Button(
                        onClick = {

                            var sellerId = fireBaseAuthViewModel.auth.currentUser?.uid

                            if (sellerId != null) {


                                    imageViewModel.uploadImageAndSaveProduct(
                                        context = context,
                                        name = pName,
                                        price = pCost,
                                        imageUri = selectedURI,
                                        sellerId = sellerId,
                                        fireStoreViewModel = fireStoreViewModel,
                                        pID =  pID

                                    )


                            }

                            ui_viewModel.makeAddProductDialogueCardHidden()


                        },
                        // modifier=Modifier.weight(1f)

                    ) {
                        Text(text = "ADD")

                    }
                }
            }

        }

        }

    }



