package com.example.video_game_collections.Screens.CustomerScreens

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun displayProductsInCurrentOrder(
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    totalOrderCost:String,
    orderId: String
) {

    val context = LocalContext.current

    var observedDisplayProductsInCurrentOrderListForCustomerState = ordersCustomerSideViewModel.displayProductsInCurrentOrderListState.observeAsState(
        emptyList<Map<String,Any>>()
    )

    LaunchedEffect(null) {
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        
       Column(
           modifier = Modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally


       ) {
           LazyColumn(
               modifier = Modifier
                   .fillMaxHeight(0.7f)
                   .fillMaxWidth()
           ) {

               items(observedDisplayProductsInCurrentOrderListForCustomerState.value){
                   Log.i("displayProductsInCurrentOrderForCustomer","displayProductsInCurrentOrder for customer invoked")

                   Card(
                       modifier = Modifier.padding(8.dp),
                       colors = if(it["status"] == OrderStatus.ACCEPTED || it["status"] == "ACCEPTED"){
                           CardDefaults.cardColors(
                               containerColor = Color.Green
                           )
                       }else if(it["status"] == OrderStatus.REJECTED ||  it["status"] == "REJECTED"){
                           CardDefaults.cardColors(
                               containerColor = Color.Red
                           )
                       }else{
                           CardDefaults.cardColors(
                               containerColor = Color.Blue
                           )
                       }

                   ) {

                       Row() {
                           AsyncImage(
                               model = it["imageURL"],
                               contentDescription = null,
                               modifier = Modifier
                                   .height(150.dp)
                                   .width(200.dp),
                               contentScale = ContentScale.Crop

                           )
                           Column {

                               Text(text = "Name: "+ it["pname"] )
                               Text(text = "Cost: "+it["pcost"] )
                               Text(text = "Quantity: "+it["quantity"] )
                               Text(text = "TotalCost: "+it["totalProductCost"] )
                               Text(text = "Status: "+it["status"])
                               TextButton(onClick = {
                                   if(it["status"] == OrderStatus.PENDING|| it["status"] == "PENDING"){
                                       ordersCustomerSideViewModel.deleteIndovidualProduct(it["pid"].toString(),orderId)
                                       Toast.makeText(context,"Product Cancelled",Toast.LENGTH_LONG).show()
                                   }else if(it["status"] == OrderStatus.ACCEPTED|| it["status"] == "ACCEPTED"){
                                       Toast.makeText(context,"Product already accepted by seller.But can cancel the entire order",Toast.LENGTH_LONG).show()

                                   }else if(it["status"] == OrderStatus.REJECTED|| it["status"] == "REJECTED"){
                                       Toast.makeText(context,"Product already rejected by seller.",Toast.LENGTH_LONG).show()

                                   }
                               }) {
                                   Text(
                                       text = "CANCEL ITEM",
                                       modifier = Modifier.border(2.dp,Color.Black)
                                   )
                               }

                           }
                       }


                   }


               }

           }

       }


        Text(text = "Total Cost of the order "+totalOrderCost.toString())




       }
    
}