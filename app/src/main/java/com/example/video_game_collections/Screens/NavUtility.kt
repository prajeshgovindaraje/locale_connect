package com.example.video_game_collections.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import kotlinx.serialization.Serializable

@Composable
fun navUtitlity(
    viewModel: fireBaseAuthViewModel,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel
) {
    var navController = rememberNavController()

    NavHost(navController = navController , startDestination = loginPage) {

        composable<loginPage> {
            loginScreen(navController,viewModel)
        }

        composable<signUpPage> {
            signInScreen(navController,viewModel)
        }

        composable<customerPage> {
            cutomerScreen(viewModel = viewModel, navController = navController)
        }

        composable<sellerPage> {
            sellerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel
            )
        }


    }

}





@Serializable
object loginPage

@Serializable
object signUpPage

@Serializable
object customerPage

@Serializable
object sellerPage