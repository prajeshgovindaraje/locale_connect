package com.example.video_game_collections.Screens.CustomerScreens

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.R
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.ordersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myOrdersScreen(
    ordersViewModel: ordersViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController
) {




    val observedOrderedPoductsState = ordersViewModel.ordersMapState.observeAsState(emptyList<Map<String,Any>>())

    LaunchedEffect(observedOrderedPoductsState.value) {
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

                        Card(
                            modifier = Modifier.padding(8.dp),
                            onClick = {

                                Log.i("myOrdersScreen",it["orderList"].toString())

                                //Display the products in the current order
                                ordersViewModel.displayProductsInCurrentOrder(it["orderList"] as MutableList<Map<String,Any>>)
                                navController.navigate(
                                    NavigationPages.displayAllProductsInCurrentOrderPage(
                                    totalOrderCost = it["totalOrderCost"].toString()
                                ))

                            }
                        ) {
                            Row {
                                var doc =  it["orderList"] as List<Map<String, Any>> // list of products in the order
                                var shopImageURL by remember{
                                    mutableStateOf("")
                                }


                                AsyncImage(
                                    model =shopImageURL,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(200.dp),
                                    contentScale = ContentScale.Crop
                                )





                                Column {

                                    var shopName by remember{
                                        mutableStateOf("")
                                    }


                                    var sellerID = doc.get(0).get("sellerID").toString()



                                            LaunchedEffect(sellerID) {
                                                if (shopName.isEmpty()) {
                                                    fireBaseAuthViewModel.getShopName(sellerID) { name ->
                                                        shopName = name
                                                    }
                                                    fireBaseAuthViewModel.getShopImage(sellerID){
                                                        shopImageURL = it
                                                    }
                                                }
                                            }




                                    Text(text = "shopName: $shopName")


                                    Text(text = "Cost: ${it["totalOrderCost"]}")
                                    Text(text = "Time: ${it["timestamp"]}")


                                    Row(
                                        modifier = Modifier.fillMaxWidth(),

                                    ) {
                                        Button(onClick = {

                                            ordersViewModel.deleteEntireOrder(
                                                orderID = it["orderId"].toString(),
                                                buyerID = it["buyerID"].toString(),
                                                orderList = it["orderList"] as MutableList<Map<String,Any>>,
                                                currentTime = it["timestamp"].toString(),
                                                totalOrderCost = it["totalOrderCost"].toString().toDouble()

                                            )

                                        }) {

                                            Text(text = "ORDER CANCEL")

                                        }

                                        Text(text = "STAtUS")
                                    }



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