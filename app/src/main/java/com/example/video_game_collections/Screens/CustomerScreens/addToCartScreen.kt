package com.example.video_game_collections.Screens.CustomerScreens

import android.widget.Toast
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
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.productOrderModel

@Composable
fun addToCartScreen(
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val buyerID = fireBaseAuthViewModel.auth.currentUser?.uid

    val observedProductsInCartState = ordersCustomerSideViewModel.productsInCartState.observeAsState(emptyList<productOrderModel>())
    val observedTotalCost = ordersCustomerSideViewModel.totalCost.observeAsState(0.0)

    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(observedProductsInCartState.value.isEmpty()){
                //Spacer(modifier = Modifier.weight(1f))
                Text(text = "NO CURRENT PRODUCTS ARE SELECTED",
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



                    items(observedProductsInCartState.value){

                        Box(modifier = Modifier.fillMaxWidth(0.9f)){

                            Row {
                                AsyncImage(
                                    model = it.imageURL,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(120.dp)
                                        .border(2.dp, Color.DarkGray),
                                    contentScale = ContentScale.Crop
                                )

                                Column {
                                    Text(text = "Name: ${it.pName}")
                                    Text(text = "cost: ${it.pCost}")
                                    Text(text = "quantity: ${it.quantity}")
                                    Text(text = "totalCost: ${it.totalProductCost}")

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

                Button(onClick = {
                    //add into orders collection
                    if (buyerID != null) {

                        if(observedProductsInCartState.value.isNotEmpty()){
                            var sellerID = observedProductsInCartState.value.get(0).sellerID

                            ordersCustomerSideViewModel.addIntoOrders(
                                observedProductsInCartState.value.toMutableList(),
                                context = context,
                                totalCost = observedTotalCost.value,
                                buyerID = buyerID,
                                status = "pending",
                                sellerID = sellerID


                            )

                        }else{
                            Toast.makeText(context,"NO PRODUCTS ARE SELECTED TO ADD",Toast.LENGTH_LONG).show()
                        }



                    }


                },
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(text = "Place Order\nTotal cost ${observedTotalCost.value}")
                }


            }


        }

    }



}