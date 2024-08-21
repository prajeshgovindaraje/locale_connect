package com.example.video_game_collections.allViewModels

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.R
import com.example.video_game_collections.Screens.NavigationPages


class UI_ViewModel : ViewModel() {


    private var _addProductDialogueCardState  = MutableLiveData<Boolean>()
    var addProductDialogueCardState : LiveData<Boolean> = _addProductDialogueCardState





    fun makeAddProductDialogueCardHidden(){
        _addProductDialogueCardState.value = false
    }

    fun makeAddProductDialogueCardVisible(){
        _addProductDialogueCardState.value = true
    }


    var select = 0

    val list = listOf(
        Icons.Default.Home,
        Icons.Default.ShoppingCart,
        R.drawable.my_orders_icon,
        Icons.Default.Person
    )
    val listNavItems= listOf(
        NavItem(
            R.drawable.baseline_home_24,
            "Home",
            NavigationPages.customerPage
        ),
        NavItem(
            R.drawable.baseline_add_shopping_cart_24,
            "Cart",
            NavigationPages.addToCartPage
        ),
        NavItem(
            R.drawable.my_orders_icon,
            "Orders",
            NavigationPages.myOrdersPage
        ),
        NavItem(
            R.drawable.baseline_person_24,
            "Profile",
            NavigationPages.customerPage
        )
    )

    data class NavItem(
        @DrawableRes val icon: Int,
        val title:String,
        val page: Any
    )



}