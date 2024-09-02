package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.R
import com.example.video_game_collections.Screens.NavigationPages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Route


class UI_ViewModel : ViewModel() {


    private var _addProductDialogueCardState  = MutableLiveData<Boolean>()
    var addProductDialogueCardState : LiveData<Boolean> = _addProductDialogueCardState


    private var _datePickerDialogState = MutableLiveData<Boolean>()
    var datePickerDialogState:LiveData<Boolean> = _datePickerDialogState


    fun makeAddProductDialogueCardHidden(){
        _addProductDialogueCardState.value = false
    }

    fun makeAddProductDialogueCardVisible(){
        _addProductDialogueCardState.value = true
    }

    fun makeDatePickerDialogVisible(){
        Log.i("dateTime", " inside make dialgue visibe fun")
        _datePickerDialogState.value = true
    }
    fun makeDatePickerDialogVHidden(){
        _datePickerDialogState.value = false
    }




    private val _currentScreen = MutableStateFlow<NavigationPages?>(null)
    val currentScreen: MutableStateFlow<NavigationPages?> get() = _currentScreen

    fun updateCurrentScreen(screen: NavigationPages) {
        _currentScreen.value = screen
    }


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
        val route: NavigationPages
    )



}