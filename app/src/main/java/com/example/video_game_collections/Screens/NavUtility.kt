package com.example.video_game_collections.Screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Composable
fun navUtitlity(
    viewModel: fireBaseAuthViewModel,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    locationViewModel : locationViewModel,
    imageViewModel: imageViewModel

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
            cutomerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                locationViewModel = locationViewModel

            )
        }

        composable<sellerPage> {
            sellerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel,
                locationViewModel = locationViewModel,
                imageViewModel = imageViewModel

            )
        }

        composable<allProductsBySellerPage> {
            allProductsBySeller(fireStoreViewModel = fireStoreViewModel)
        }

        composable<permissionDeniedPage> {
            permissionDeniedScreen(locationViewModel,navController, authViewModel = viewModel)

        }

        composable<allProductsForCustomerPage> {
            allProductsForCustomer(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                locationViewModel = locationViewModel
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

@Serializable
object allProductsBySellerPage

@Serializable
data class productDescriptionPage(
        val pName : String,
        val pCost : Int,
        val sellerId : String

        )

@Serializable
object permissionDeniedPage

@Serializable
object allProductsForCustomerPage

