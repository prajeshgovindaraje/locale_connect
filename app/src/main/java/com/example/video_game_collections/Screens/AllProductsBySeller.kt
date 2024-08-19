package com.example.video_game_collections.Screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

    var shopName by remember {
        mutableStateOf("")
    }

    var context = LocalContext.current


    Text(text = "All products by  a seller")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(observeAllProductsBySellerState.value){
            Log.i("shopName"," inside your ")
            authViewModel.getShopName(it.sellerID){
                     shopName = it
            }

            Card(
                modifier = Modifier.fillMaxWidth(0.8f).padding(16.dp),


            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = it.imageURL,
                        contentDescription = null,
                       // modifier = Modifier.height(200.dp).width(300.dp),
                        contentScale = ContentScale.Crop

                    )
                    Text(text = it.pName)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = it.pCost.toString())
                    Log.i("shopName"," ${shopName}")
                    Text(text = shopName)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        //product edit button
                        IconButton(onClick = { /*TODO*/ }) {

                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )


                        }


                        //product delete button
                        IconButton(onClick = {

                            fireStoreViewModel.deleteFromdb(it.pName,context,it.sellerID)
                        }) {

                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )


                        }
                    }



                }

            }

        }


    }

}