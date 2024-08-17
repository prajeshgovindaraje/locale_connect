package com.example.video_game_collections.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.dataModels.productModel

@Composable
fun allProductsBySeller(
    modifier: Modifier = Modifier,
    fireStoreViewModel: fireStoreViewModel,
    authViewModel: fireBaseAuthViewModel
) {

    var observeAllProductsBySellerState = fireStoreViewModel.allProductsBySellerState.observeAsState(
        emptyList<productModel>()
    )

   LaunchedEffect(null) {

       var userID =  authViewModel.auth.currentUser?.uid


   }

    Text(text = "All products by  a seller")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(observeAllProductsBySellerState.value){

            Card(
                modifier = Modifier.padding(16.dp),

            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = it.imageURL,
                        contentDescription = null,
                    )
                    Text(text = it.pName)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = it.pCost.toString())


                }

            }

        }


    }

}