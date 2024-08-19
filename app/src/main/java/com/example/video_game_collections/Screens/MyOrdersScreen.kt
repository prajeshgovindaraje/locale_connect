package com.example.video_game_collections.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.ordersViewModel

@Composable
fun myOrdersScreen(
    ordersViewModel: ordersViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController
) {




    val observedOrderedPoductsState = ordersViewModel.ordersMapState.observeAsState(emptyList<Map<String,Any>>())

    LaunchedEffect(null) {
        var userID =  fireBaseAuthViewModel.auth.currentUser?.let { ordersViewModel.displayCurrentUserOrders(it.uid) }



    }

    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(observedOrderedPoductsState.value.isEmpty()){
                //Spacer(modifier = Modifier.weight(1f))
                Text(text = "NO CURRENT ORDERS ARE MADE BY YOU",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxHeight(0.7f)
                )
            }

            else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .fillMaxWidth()
                ) {



                    items(observedOrderedPoductsState.value){

                        Box(modifier = Modifier.fillMaxWidth(0.9f)){

                            Row {




                                Column {


                                    for (doc in it["orderList"] as List<Map<String, Any>>) {
                                        val sellerID = doc["sellerID"].toString()

                                        var shopName by remember(sellerID) { mutableStateOf("") }


                                        // Assuming getShopName is a suspend function, you might want to use LaunchedEffect or rememberCoroutineScope here
                                        LaunchedEffect(sellerID) {
                                            if(shopName.isEmpty()){
                                                fireBaseAuthViewModel.getShopName(sellerID) { it ->
                                                    shopName = it
                                                }
                                            }

                                        }
                                        Text(text = "shopName: ${shopName}")




                                    }

                                    Text(text = "Cost: ${it["totalOrderCost"]}")
                                    Text(text = "Time: ${it["timestamp"]}")



                                }

                            }

                        }

                    }

                }
            }



            Row(

                modifier = Modifier
                    .fillMaxWidth(),
                //  .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,

                ) {
                Button(onClick = {
                    navController.popBackStack()
                },
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(text = "CANCEL")

                }




            }


        }

    }

}