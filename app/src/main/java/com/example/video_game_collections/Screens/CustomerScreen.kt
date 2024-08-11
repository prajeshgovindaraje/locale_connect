package com.example.video_game_collections.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.loginStatus

@Composable
fun cutomerScreen(modifier: Modifier = Modifier, viewModel: fireBaseAuthViewModel, navController: NavController) {


    val observedLoginStatus  = viewModel.loginStatusState.observeAsState()
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
            TextButton(onClick = {
                viewModel.signOut()
            }) {
                Text(text = "SIGN OUT")
            }
        }
    }

}