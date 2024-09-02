package com.example.video_game_collections.Screens.CustomerScreens


import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.Screens.AutoResizedText
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.Screens.NavigationPages.allProductsForCustomerByThisSellerPage
import com.example.video_game_collections.allViewModels.AllProductwithSellerID
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productModel
import com.example.video_game_collections.dataModels.productOrderModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun allProductsForCustomerByThisSeller(
    navController: NavController,
    modifier: Modifier = Modifier,
    fireStoreViewModel: fireStoreViewModel,
    authViewModel: fireBaseAuthViewModel,
    ordersCustomerSideViewModel: ordersCustomerSideViewModel
) {

    
    val observeAllProductsBySellerState = fireStoreViewModel.allProductwithSellerID.observeAsState(
        emptyList<AllProductwithSellerID>()
    )

    var observedProductsCountMapState = ordersCustomerSideViewModel.productsCountMapState.observeAsState(
        emptyMap<String,Int>()
    )

    var shopName by remember {
        mutableStateOf("")
    }

    val sellerId = observeAllProductsBySellerState.value.find { it is AllProductwithSellerID.sellerId } as? AllProductwithSellerID.sellerId
    val products = observeAllProductsBySellerState.value.filterIsInstance<AllProductwithSellerID.product>()
    val seller : String = sellerId!!.id





    var buyerId = authViewModel.auth.currentUser?.uid

    var obserevedTotalCost = ordersCustomerSideViewModel.totalCost.observeAsState()




    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {


                authViewModel.getShopName(seller){
                    shopName = it
                }
                Text(
                    text = "${shopName}",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceTint),
            modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            )
        }
    ) { innerpadding ->

            Log.i(
                "changeOrder",
                "allProductsForCustomerByThisSeller pagw  " + observeAllProductsBySellerState.value.toString()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .padding(top = 20.dp, bottom = 80.dp),

            ) {

                        if (products.isNotEmpty()) {
                            items(products) { productItem ->
                                val product = productItem.product
                                Log.i("shopName", " inside your ")



                                ProductCard(
                                    product = product,
                                    ordersCustomerSideViewModel,
                                    observedProductsCountMapState,
                                    buyerId
                                )
                            }
                        }
                else{
                    item(span = { GridItemSpan(2) }) { Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){ Text(text = "No Products added yet")} }
                        }


                item (span = { GridItemSpan(2) }){ Spacer(modifier = Modifier.height(50.dp)) }
                item(span ={ GridItemSpan(2) },){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = {

                                navController.navigate(NavigationPages.addToCartPage)

                            }) {

                                Text(text = "Check Out Page/ Cart Page")

                            }
                            Text(text = "Total Cost : ${obserevedTotalCost.value}")
                        }
                }


            }


        }
}





@Composable
fun ProductCard(
    product: productModel,
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    observedProductsCountMapState: State<Map<String, Int>>,
    buyerId: String?
){

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier
            .height(320.dp)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, bottom = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                ),
                contentAlignment = Alignment.Center

        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .offset(y = 35.dp, x = 3.dp)
            ){
                IconButton(onClick = {

                    var count = ordersCustomerSideViewModel.getCurrCount(product.pName)

                    ordersCustomerSideViewModel.incrementOrderCount(product.pName,count) // changes the obsereved map
                    Log.i("orderIncDec","${ordersCustomerSideViewModel.getCurrCount(product.pName)} is ${product.pName}")

                    var tempProductOrderModel = productOrderModel(
                        pName = product.pName,
                        pCost = product.pCost,
                        quantity  = ordersCustomerSideViewModel.getCurrCount(product.pName),
                        totalProductCost =  product.pCost*ordersCustomerSideViewModel.getCurrCount(product.pName),
                        sellerID = product.sellerID,
                        imageURL  = product.imageURL,
                        buyerID = buyerId,
                        status = OrderStatus.PENDING,
                        pID = product.pID


                    )

                    ordersCustomerSideViewModel.addOrUpdateToProductsInCartList(tempProductOrderModel)

                    ordersCustomerSideViewModel.addToTotalCost(product.pCost)

                },
                    modifier = Modifier.background(MaterialTheme.colorScheme.onSecondaryContainer,CircleShape))
                {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .offset(y = 35.dp, x = (-3).dp),
                contentAlignment = Alignment.TopEnd
            ){
                IconButton(onClick = {

                    var count = ordersCustomerSideViewModel.getCurrCount(product.pName)

                    // changes the obsereved map
                    if(count > 0){
                        ordersCustomerSideViewModel.decrementCount(product.pName,count)
                    }
                    Log.i("orderIncDec","${ordersCustomerSideViewModel.getCurrCount(product.pName)} is ${product.pName}")

                    var tempProductOrderModel = productOrderModel(
                        pName = product.pName,
                        pCost = product.pCost,
                        quantity  = ordersCustomerSideViewModel.getCurrCount(product.pName),
                        totalProductCost =  product.pCost*ordersCustomerSideViewModel.getCurrCount(product.pName),
                        sellerID = product.sellerID,
                        imageURL  = product.imageURL,
                        buyerID = buyerId,
                        status = OrderStatus.PENDING,
                        pID = product.pID


                    )

                    if(ordersCustomerSideViewModel.getCurrCount(product.pName) == 0){
                        ordersCustomerSideViewModel.removeProductsInCartList(tempProductOrderModel)
                    }else{
                        ordersCustomerSideViewModel.addOrUpdateToProductsInCartList(tempProductOrderModel)
                    }

                    if(ordersCustomerSideViewModel.getCurrCount(product.pName) >= 0){
                        ordersCustomerSideViewModel.reduceFromTotalCost(product.pCost)

                    }
                },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSecondaryContainer, CircleShape)
                        .padding()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    AutoResizedText(
                        text = product.pName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                    Text(
                        text = "₹${product.pCost}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.End,
                    )
                    if(observedProductsCountMapState.value.get(product.pName) == null){
                        Text(text = "0", style = MaterialTheme.typography.bodyMedium )

                    }else{
                        Text(text =  observedProductsCountMapState.value.get(product.pName).toString(), style = MaterialTheme.typography.bodyLarge)

                    }
                }
            }



        }
        Box(
            modifier = Modifier
                .height(190.dp)
                .padding(bottom = 10.dp, start = 10.dp, end = 15.dp)
                .onSizeChanged { imageSize = it },
        ) {
            AsyncImage(
                model = product.imageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
            )
        }
    }
}


