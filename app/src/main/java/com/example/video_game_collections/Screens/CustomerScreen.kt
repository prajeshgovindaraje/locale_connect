package com.example.video_game_collections.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.loginStatus
import com.example.video_game_collections.dataModels.productModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cutomerScreen(
    modifier: Modifier = Modifier,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel
) {

    val obserevedAllProductsForCustomerState = fireStoreViewModel.allProductsForCustomerState.observeAsState(
        emptyList<productModel>()
    )
    val observedLoginStatus  = fireBaseAuthViewModel.loginStatusState.observeAsState()
    var context = LocalContext.current

    LaunchedEffect(key1 = observedLoginStatus.value) {

        when(observedLoginStatus.value){
            //is loginStatus.Error -> TODO()
            is loginStatus.LoggedIn -> {
                //do nothing

            }
            is loginStatus.LoggedOut -> {

                navController.navigate(loginPage)

            }
            else -> Unit
        }

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
                text = "Customer Screen",
                fontSize = 30.sp
            )
            
            Spacer(modifier = Modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2)
            ) {
                items(obserevedAllProductsForCustomerState.value){



                    Card(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(text = it.pName)
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = it.pCost.toString())

                        }
                    }


                }
            }
            
            
            
            
            TextButton(onClick = {
                fireBaseAuthViewModel.signOut()
            }) {
                Text(text = "SIGN OUT")
            }
        }
    }

}