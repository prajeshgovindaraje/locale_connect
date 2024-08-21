package com.example.video_game_collections.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.video_game_collections.Screens.CustomerScreens.addToCartScreen
import com.example.video_game_collections.Screens.CustomerScreens.allProductsForCustomer
import com.example.video_game_collections.Screens.CustomerScreens.allProductsForCustomerByThisSeller
import com.example.video_game_collections.Screens.CustomerScreens.cutomerScreen
import com.example.video_game_collections.Screens.CustomerScreens.displayProductsInCurrentOrder
import com.example.video_game_collections.Screens.CustomerScreens.myOrdersScreen
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.fireStoreViewModel
import com.example.video_game_collections.allViewModels.imageViewModel
import com.example.video_game_collections.allViewModels.locationViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.allViewModels.ordersSellerSideViewModel
import kotlinx.serialization.Serializable

@Composable
fun navUtitlity(
    viewModel: fireBaseAuthViewModel,
    fireStoreViewModel: fireStoreViewModel,
    ui_viewModel: UI_ViewModel,
    locationViewModel : locationViewModel,
    imageViewModel: imageViewModel,
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    navController: NavHostController,
    ordersSellerSideViewModel:ordersSellerSideViewModel,
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
            cutomerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                locationViewModel = locationViewModel

            )
        }

        composable<NavigationPages.sellerPage> {
            sellerScreen(
                fireBaseAuthViewModel = viewModel,
                navController = navController,
                fireStoreViewModel = fireStoreViewModel,
                ui_viewModel = ui_viewModel,
                locationViewModel = locationViewModel,
                imageViewModel = imageViewModel,
                ordersSellerSideViewModel = ordersSellerSideViewModel


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
                ordersCustomerSideViewModel = ordersCustomerSideViewModel
            )
        }

        composable<NavigationPages.addToCartPage> {
            addToCartScreen(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                navController = navController,
                fireBaseAuthViewModel = viewModel
            )
        }

        composable<NavigationPages.myOrdersPage> {
            myOrdersScreen(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                navController = navController,
                fireBaseAuthViewModel = viewModel
            )
        }

        composable<NavigationPages.display_All_Products_In_CurrentOrder_ForCustomer_Page> {
            val args = it.toRoute<NavigationPages.display_All_Products_In_CurrentOrder_ForCustomer_Page>()
            displayProductsInCurrentOrder(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                totalOrderCost = args.totalOrderCost


            )
        }

        composable<NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page> {
            val args = it.toRoute<NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page>()
            displayProductsInCurrentOrderForSeller(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                totalOrderCost = args.totalOrderCost,
                orderID = args.orderID,
                ordersSellerSideViewModel = ordersSellerSideViewModel


            )
        }

        composable<NavigationPages.sellerOrderScreenPage> {
            sellerOrderScreen(
                ordersSellerSideViewModel = ordersSellerSideViewModel,
                fireStoreViewModel = fireStoreViewModel,
                fireBaseAuthViewModel = viewModel,
                ordersCustomerSideViewModel= ordersCustomerSideViewModel,
                navController = navController

            )
        }


    }

}



@Serializable
sealed class NavigationPages{



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

    @Serializable
    object allProductsForCustomerByThisSellerPage


    @Serializable
    object addToCartPage

    @Serializable
    object myOrdersPage

    @Serializable
    data class display_All_Products_In_CurrentOrder_ForCustomer_Page(
        val totalOrderCost : String
    )

    @Serializable
    data class display_All_Products_In_CurrentOrder_ForSeller_Page(
        val totalOrderCost : String,
        val orderID : String

    )



    @Serializable
    object sellerOrderScreenPage







}



