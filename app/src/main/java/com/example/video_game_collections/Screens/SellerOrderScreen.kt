package com.example.video_game_collections.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.allViewModels.ordersSellerSideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sellerOrderScreen(
        ordersSellerSideViewModel: ordersSellerSideViewModel,
        fireStoreViewModel: fireStoreViewModel,
        fireBaseAuthViewModel: fireBaseAuthViewModel,
        ordersCustomerSideViewModel: ordersCustomerSideViewModel,
        navController: NavController


) {

    var currSellerID = fireBaseAuthViewModel.auth.currentUser?.uid

    var observedDisplayOrdersToSellerListState =
        ordersSellerSideViewModel.displayOrdersToSellerListState.observeAsState(
            initial = emptyList<Map<String, Any>>()

        )



    LaunchedEffect(null) {



    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (observedDisplayOrdersToSellerListState.value.isEmpty()) {
                //Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "NO CURRENT ORDERS ARE MADE FOR YOU",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxHeight(0.7f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .fillMaxWidth()
                ) {


                    items(observedDisplayOrdersToSellerListState.value) {

                        Card(
                            modifier = Modifier.padding(8.dp),
                            onClick = {


                                //Display the products in the current order
                                ordersCustomerSideViewModel.displayProductsInCurrentOrder(it["orderList"] as MutableList<Map<String,Any>>)
                                navController.navigate(
                                    NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page(
                                        totalOrderCost = it["totalOrderCost"].toString(),
                                        orderID = it["orderId"].toString()
                                    ))

                            }


                        ) {
                            Row() {

                                var buyerName by remember {

                                    mutableStateOf("ioo")
                                }

                                if (currSellerID != null) {
                                    fireBaseAuthViewModel.getUserName(it.get("buyerID").toString()){

                                        buyerName = it

                                    }
                                }



                                var orderProductsOnlyList =
                                    it["orderList"] as List<Map<String, Any>>
                                var firstProductImageURL = orderProductsOnlyList[0].get("imageURL")

                                AsyncImage(
                                    model = firstProductImageURL,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(200.dp),
                                    contentScale = ContentScale.Crop
                                )

                                Column {
                                    Text(text = buyerName)

                                    Text(text = "Total Cost: "+it["totalOrderCost"].toString())
                                    Text(text = "TimeOfOrder: "+it["timestamp"].toString())



                                }

                            }
                        }

                    }

                }
            }


        }
    }
}



