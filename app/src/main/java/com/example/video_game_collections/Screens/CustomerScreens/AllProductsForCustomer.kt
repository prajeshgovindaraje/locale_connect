package com.example.video_game_collections.Screens.CustomerScreens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.R
import com.example.video_game_collections.Screens.AutoResizedText
import com.example.video_game_collections.Screens.NavigationPages
import com.example.video_game_collections.Screens.NavigationPages.allProductsForCustomerByThisSellerPage
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
        fireBaseAuthViewModel.auth.currentUser?.let {
            fireStoreViewModel.displayAllProductsforCustomer(it.uid)
        }

    }

    val obserevedAllProductsForCustomerState = fireStoreViewModel.allProductsForCustomerState.observeAsState(
        emptyList<productModel>()
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        )
    {
        items(obserevedAllProductsForCustomerState.value){product ->
            ProductCard(
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                fireBaseAuthViewModel = fireBaseAuthViewModel,
                product = product,
                modifier = Modifier

            )
        }

    }
    



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(modifier: Modifier = Modifier,navController: NavController,fireStoreViewModel: fireStoreViewModel,fireBaseAuthViewModel: fireBaseAuthViewModel,product: productModel) {
    var shopName by remember {

        mutableStateOf("")

    }

    fireBaseAuthViewModel.getShopName(product.sellerID){
        shopName = it
    }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }


    Box(
        modifier = Modifier
            .height(275.dp)
            .padding(5.dp)
            .clickable {

            }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(20.dp)
                )

        )
        Box (
            modifier = Modifier
                .height(190.dp)
                .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
                .onSizeChanged { imageSize = it }
            ,
        ){
            AsyncImage(
                model = product.imageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                ,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
            ,
            contentAlignment = Alignment.BottomStart
        ){
            Column {
                AutoResizedText(
                    text = product.pName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(5.dp))
                AutoResizedText(
                    text = shopName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
            ,
            contentAlignment = Alignment.BottomEnd
        ){
            Text(
                text = "${product.pCost}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}