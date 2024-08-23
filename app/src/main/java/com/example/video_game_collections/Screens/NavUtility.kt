package com.example.video_game_collections.Screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.video_game_collections.Screens.CustomerScreens.addToCartScreen
import com.example.video_game_collections.Screens.CustomerScreens.allProductsForCustomer
import com.example.video_game_collections.Screens.CustomerScreens.cutomerScreen
import com.example.video_game_collections.Screens.CustomerScreens.displayProductsInCurrentOrder
import com.example.video_game_collections.Screens.CustomerScreens.myOrdersScreen
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.ordersViewModel
import kotlinx.serialization.Serializable
import okhttp3.Route

@Composable
fun navUtitlity(
    viewModel: fireBaseAuthViewModel,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    locationViewModel : locationViewModel,
    imageViewModel: imageViewModel,
    ordersViewModel: ordersViewModel,
    navController: NavHostController,
    modifier: Modifier

) {

    NavHost(navController = navController , startDestination = NavigationPages.loginPage) {

        composable<NavigationPages.loginPage> {
            loginScreen(navController,viewModel)
        }

        composable<NavigationPages.signUpPage> {
            signInScreen(navController,viewModel)
        }

        composable<NavigationPages.customerPage> {
            ui_viewModel.updateCurrentScreen(NavigationPages.customerPage)
            cutomerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                locationViewModel = locationViewModel,
                viewModel = ui_viewModel
            )
        }

        composable<NavigationPages.sellerPage> {
            sellerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel,
                locationViewModel = locationViewModel,
                imageViewModel = imageViewModel

            )
        }

        composable<NavigationPages.allProductsBySellerPage> {
            allProductsBySeller(fireStoreViewModel = fireStoreViewModel, authViewModel = viewModel)
        }

        composable<NavigationPages.permissionDeniedPage> {
            permissionDeniedScreen(locationViewModel,navController, authViewModel = viewModel)

        }

        composable<NavigationPages.allProductsForCustomerPage> {
            allProductsForCustomer(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                locationViewModel = locationViewModel
            )
        }

        composable<NavigationPages.allProductsForCustomerByThisSellerPage> {
            allProductsForCustomerByThisSeller(
                fireStoreViewModel = fireStoreViewModel,
                authViewModel = viewModel,
                navController = navController,
                ordersViewModel = ordersViewModel
            )
        }

        composable<NavigationPages.addToCartPage> {
            ui_viewModel.updateCurrentScreen(NavigationPages.addToCartPage)
            addToCartScreen(
                ordersViewModel = ordersViewModel,
                navController = navController,
                fireBaseAuthViewModel = viewModel
            )
        }

        composable<NavigationPages.myOrdersPage>{
            ui_viewModel.updateCurrentScreen(NavigationPages.myOrdersPage)
            myOrdersScreen(
                ordersViewModel = ordersViewModel,
                navController = navController,
                fireBaseAuthViewModel = viewModel
            )
        }

        composable<NavigationPages.displayAllProductsInCurrentOrderPage> {
            val args = it.toRoute<NavigationPages.displayAllProductsInCurrentOrderPage>()
            displayProductsInCurrentOrder(
                ordersViewModel = ordersViewModel,
                totalOrderCost = args.totalOrderCost


            )
        }


    }

}



@Serializable
sealed class NavigationPages(){



    @Serializable
    object loginPage

    @Serializable
    object signUpPage

    @Serializable
    object customerPage:NavigationPages()

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
    object allProductsForCustomerPage:NavigationPages()

    @Serializable
    object allProductsForCustomerByThisSellerPage


    @Serializable
    object addToCartPage:NavigationPages()

    @Serializable
    object myOrdersPage:NavigationPages()

    @Serializable
    data class displayAllProductsInCurrentOrderPage(
        val totalOrderCost : String
    )

}






