package com.example.video_game_collections.Screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.loginStatus

@Composable
fun sellerScreen(
    modifier: Modifier = Modifier,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    navController: NavController,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel
) {

    val observedAddProductDialogueCardState = ui_viewModel.addProductDialogueCardState.observeAsState(initial = false)
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
                text = "Seller Screen",
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(30.dp))


            IconButton(onClick = {

                    ui_viewModel.makeAddProductDialogueCardVisible()

            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }


            Spacer(modifier = Modifier.height(30.dp))




            TextButton(onClick = {
                fireBaseAuthViewModel.signOut()
            }) {
                Text(text = "SIGN OUT")
            }
        }

        if(observedAddProductDialogueCardState.value == true){
            addProductDialogueCard(
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel,
                fireBaseAuthViewModel = fireBaseAuthViewModel
            )
        }
    }

}


@Composable
fun addProductDialogueCard(
    modifier: Modifier = Modifier,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel
) {

    var pName by remember {
        mutableStateOf("")
    }
    var pCost by remember {
        mutableStateOf(0)
    }

    Dialog(
        onDismissRequest = { ui_viewModel.makeAddProductDialogueCardHidden() },

        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {


        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(0.9f)
                .border(1.dp, Color.Cyan, RoundedCornerShape(15.dp))
        ) {

            Text(text = "ADD YOUR PRODUCT")

            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = pName, onValueChange = {

                    pName = it

                },
                label = {
                    Text(text = "Product Name")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = pCost.toString(), onValueChange = {

                    if(it.isNotEmpty()){
                        pCost = it.toInt()

                    }else{

                    }

                },

                label = {
                    Text(text = "Product Cost")
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(

                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = {

                        ui_viewModel.makeAddProductDialogueCardHidden()

                    },
                    //modifier=Modifier.weight(1f)
                ) {
                    Text(text = "BACK")

                }

                Button(
                    onClick = {

                        var sellerId = fireBaseAuthViewModel.auth.currentUser?.uid

                        if (sellerId != null) {
                            fireStoreViewModel.addProductsIntoDB(pname = pName, pcost = pCost, sellerId = sellerId)
                        }

                       ui_viewModel.makeAddProductDialogueCardHidden()


                    },
                    // modifier=Modifier.weight(1f)

                ) {
                    Text(text = "ADD")

                }
            }

        }

        }

    }



