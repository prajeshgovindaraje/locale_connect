package com.example.video_game_collections.Screens.CustomerScreens

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun displayProductsInCurrentOrder(
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    totalOrderCost:String
) {

    var observedDisplayProductsInCurrentOrderListState = ordersCustomerSideViewModel.displayProductsInCurrentOrderListState.observeAsState(
        emptyList<Map<String,Any>>()
    )
    
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

               items(observedDisplayProductsInCurrentOrderListState.value){

                   Card(
                       modifier = Modifier.padding(8.dp)

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

                           }
                       }


                   }


               }

           }

       }


        Text(text = "Total Cost of the order "+totalOrderCost.toString())




       }
    
}