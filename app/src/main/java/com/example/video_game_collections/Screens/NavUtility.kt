package com.example.video_game_collections.Screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.video_game_collections.Screens.CustomerScreens.ProfileScreen
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
import com.example.video_game_collections.dataModels.productOrderModel
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


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
            ui_viewModel.updateCurrentScreen(NavigationPages.addToCartPage)
            addToCartScreen(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                navController = navController,
                fireBaseAuthViewModel = viewModel,
                uiViewmodel = ui_viewModel

            )
        }

        composable<NavigationPages.myOrdersPage>{
            ui_viewModel.updateCurrentScreen(NavigationPages.myOrdersPage)
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
                totalOrderCost = args.totalOrderCost,
                orderId = args.orderID



            )
        }

        composable<NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page> {
            val args = it.toRoute<NavigationPages.display_All_Products_In_CurrentOrder_ForSeller_Page>()

           // val jsonListOfProductsInOrder = args.listOfProducts

            // Deserialize the JSON back to MutableList<Map<String, Any>>
          //  val listOfProductsInOrder: MutableList<productOrderModel> =
             //   Json.decodeFromString(jsonListOfProductsInOrder)

            displayProductsInCurrentOrderForSeller(
                ordersCustomerSideViewModel = ordersCustomerSideViewModel,
                totalOrderCost = args.totalOrderCost,
                orderID = args.orderID,
                ordersSellerSideViewModel = ordersSellerSideViewModel,
                buyerID = args.buyerID,
               // listOfProductsInOrder = listOfProductsInOrder


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

        composable<NavigationPages.ProfilePage> {
            ProfileScreen(
                fireStoreViewModel = fireStoreViewModel,
                fireBaseAuthViewModel = viewModel,
                navController = navController
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
    data class display_All_Products_In_CurrentOrder_ForCustomer_Page(
        val totalOrderCost : String,
        val orderID: String
    )

    @Serializable
    data class display_All_Products_In_CurrentOrder_ForSeller_Page(
        val totalOrderCost : String,
        val orderID : String,
        val buyerID : String,

    )



    @Serializable
    object sellerOrderScreenPage:NavigationPages()

    @Serializable
    object ProfilePage: NavigationPages()

}








