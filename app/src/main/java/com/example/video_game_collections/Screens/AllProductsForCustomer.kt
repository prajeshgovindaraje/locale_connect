package com.example.video_game_collections.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.dataModels.productModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun allProductsForCustomer(
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel,
    locationViewModel : locationViewModel
) {



    LaunchedEffect(null) {
        fireBaseAuthViewModel.auth.currentUser?.let { fireStoreViewModel.displayAllProductsforCustomer(it.uid) }

    }

    val obserevedAllProductsForCustomerState = fireStoreViewModel.allProductsForCustomerState.observeAsState(
        emptyList<productModel>()
    )

    var count by remember {
        mutableStateOf(0)
    }
    
    var shopName by remember {
        
        mutableStateOf("")
        
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "All products for Customer Screen",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(obserevedAllProductsForCustomerState.value){
                    
                    fireBaseAuthViewModel.getShopName(it.sellerID){
                        shopName = it
                    }



                    Card(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(model = it.imageURL, contentDescription = null)
                            Text(text = it.pName)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = it.pCost.toString())
                            Text(text = "SHOP NAME: ${shopName}")


                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(onClick = {
                                    count++
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null
                                    )

                                }
                                Text(text = count.toString())
                                IconButton(onClick = {
                                    count--
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null
                                    )

                                }
                            }

                        }
                    }


                }
            }





        }
    }



}