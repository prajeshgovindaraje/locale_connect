package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ordersSellerSideViewModel: ViewModel(){

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    private var _displayOrdersToSellerListState = MutableLiveData<MutableList<Map<String,Any>>>()
    var displayOrdersToSellerListState : LiveData<MutableList<Map<String,Any>>> = _displayOrdersToSellerListState



    var tempDisplayOrdersToSellerListState = mutableListOf<Map<String,Any>>()
    fun displayOrderForCurrentSeller(sellerID : String){


        Log.i("displayOrderForCurrentSeller","display order for current Seller")

        db.collection("orders")
            .whereEqualTo("sellerID",sellerID)
            .addSnapshotListener { value, error ->
                tempDisplayOrdersToSellerListState.clear()

                if (value != null) {
                    for(orders in value){

                        tempDisplayOrdersToSellerListState += orders.data as Map<String,Any>


                    }
                }

                _displayOrdersToSellerListState.value = tempDisplayOrdersToSellerListState.toMutableList()
            }

    }







}