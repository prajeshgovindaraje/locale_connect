package com.example.video_game_collections.Screens.CustomerScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.productOrderModel
import com.example.video_game_collections.ui.theme.tertiaryDark

@OptIn(ExperimentalMaterial3Api::class)
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



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Cart", style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiary)},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
        }
    )
    {innerpadding->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding)
            .padding(top = 3.dp)

        ) {


            if (observedProductsInCartState.value.isEmpty()) {
                //Spacer(modifier = Modifier.weight(1f))

                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "NO CURRENT\n PRODUCTS ARE SELECTED",
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxHeight(0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
            } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(560.dp)

                    ) {


                        items(observedProductsInCartState.value) {

                            OrderCard(order = it)
                        }

                    }
                }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
                ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .height(210.dp)
                        .padding(bottom = 100.dp),
                    contentAlignment = Alignment.BottomCenter
                )
                {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 55.dp, start = 70.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "Total:", style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "${observedTotalCost.value}", style = MaterialTheme.typography.headlineLarge)
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(.9f)
                                .background(
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
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
                                                sellerID = sellerID,
                                                )

                                        }else{
                                            Toast.makeText(context,"NO PRODUCTS ARE SELECTED TO ADD",Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = "Place Order", color = MaterialTheme.colorScheme.onTertiaryContainer)
                        }
                        
                    }

            }

        }
    }

}


@Composable
fun OrderCard(modifier: Modifier = Modifier, order : productOrderModel) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(40.dp)
            )


    ){

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            AsyncImage(
                model = order.imageURL,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(.96f)
                    .clip(RoundedCornerShape(40.dp)),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.8f)
                    .padding(top = 15.dp)
            ){
                Text(text = order.pName)
                Text(text = "${order.pCost}")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(),
                    contentAlignment = Alignment.BottomCenter,

                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 60.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 15.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(text = "x${order.quantity}", color = MaterialTheme.colorScheme.surface)
                        }
                    }
                }
            }

        }

    }

}