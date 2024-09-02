package com.example.video_game_collections.Screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.allViewModels.ordersSellerSideViewModel
import com.example.video_game_collections.helperFunctions.timestampToReadable
import com.google.firebase.Timestamp
import kotlinx.serialization.Contextual
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import listOfMapsToProductOrderModels

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

                       if(observedDisplayOrdersToSellerListState.value.isNotEmpty()){
                           Card(
                               modifier = Modifier.padding(8.dp),

                               colors =if(it["isCancelledBySeller"] == true || it["isCancelledByCustomer"] == true ) {
                                   CardDefaults.cardColors(containerColor = Color.LightGray)

                               }else{
                                   CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)

                               },


                               onClick = {

                                   val productModelList = listOfMapsToProductOrderModels(it["orderList"] as MutableList<Map<String,Any>>)


                                   //Display the products in the current order
                                   //  val jsonOrderList = Json.encodeToString(productModelList)
                                   ordersCustomerSideViewModel.displayProductsInCurrentOrder(it["orderList"] as MutableList<Map<String,Any>>)

                                   // ordersCustomerSideViewModel.displayProductsInCurrentOrder(it["orderList"] as MutableList<Map<String,Any>>)
                                   navController.navigate(
                                       NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page(
                                           totalOrderCost = it["totalOrderCost"].toString(),
                                           orderID = it["orderId"].toString(),
                                           buyerID = it["buyerID"].toString(),

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
                                   var firstProductImageURL = if(orderProductsOnlyList.isNotEmpty()){
                                       orderProductsOnlyList[0].get("imageURL")
                                   }else{
                                       null
                                   }

                                   AsyncImage(
                                       model = firstProductImageURL,
                                       contentDescription = null,
                                       modifier = Modifier
                                           .height(150.dp)
                                           .width(200.dp),
                                       contentScale = ContentScale.Crop
                                   )
                                   var countList by remember { mutableStateOf(listOf(0, 0, 0)) }
                                   // Calculate status counts
                                   ordersCustomerSideViewModel.countStatus(orderProductsOnlyList) {
                                       countList = it
                                   }

                                   var totalProductsInTheOrder = orderProductsOnlyList.size


                                   val pendingCnt = countList.get(0) //pending
                                   val acceptedCnt = countList.get(1) // accepted
                                   val rejectedCnt = countList.get(2) // rejected


                                    if(rejectedCnt == totalProductsInTheOrder){
                                        ordersSellerSideViewModel.makeSellerCancelTrue(it["orderId"].toString())
                                    }



                                    val formattedDate:String = timestampToReadable(it["timestamp"] as Timestamp)


                                   Column {
                                       Text(text = buyerName)

                                       Text(text = "Total Cost: "+it["totalOrderCost"].toString())
                                       Text(text = "Time of Deleivery: $formattedDate")
                                       Text(
                                           text = "Pending: ${pendingCnt}/${totalProductsInTheOrder} ",
                                           color = Color.Red
                                       )
                                       Text(
                                           text = "Accepted: ${acceptedCnt}/${totalProductsInTheOrder} ",
                                           color = Color.Red
                                       )
                                       Text(
                                           text = "Rejected: ${rejectedCnt}/${totalProductsInTheOrder} ",
                                           color = Color.Red
                                       )

                                       if(it["isCancelledByCustomer"] == false && it["isCancelledBySeller"]==false){
                                           if(acceptedCnt == 0){
                                               Button(onClick = {
                                                   ordersSellerSideViewModel.makeSellerCancelTrue(it["orderId"].toString())


                                               }) {
                                                   Text("Reject Order")
                                               }
                                           }else{
                                               Text("Cannot Reject some Accepted")
                                           }
                                       }else{

                                           if(it["isCancelledByCustomer"] == true && it["isCancelledBySeller"]==false){
                                               Text(text = "Order Cancelled By Customer")
                                           }else if(it["isCancelledByCustomer"] == false && it["isCancelledBySeller"]==true){
                                               Text(text = "Order Cancelled By Seller")

                                           }
                                           Button(onClick = {
                                               if(it["isRemovedByCustomer"] == true){

                                                   ordersCustomerSideViewModel.deleteEntireOrder(it["orderId"].toString())

                                               }else{
                                                   ordersSellerSideViewModel.makeSellerRemoveTrue(it["orderId"].toString())

                                               }


                                           }) {
                                               Text(text = "Remove")
                                           }

                                       }



                                   }

                               }
                           }
                       }

                    }

                }
            }


        }
    }
}



