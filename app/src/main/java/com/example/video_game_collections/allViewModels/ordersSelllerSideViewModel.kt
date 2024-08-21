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
            .get()
            .addOnSuccessListener {

                tempDisplayOrdersToSellerListState.clear()

                for(orders in it){

                    tempDisplayOrdersToSellerListState += orders.data as Map<String,Any>



                }

                _displayOrdersToSellerListState.value = tempDisplayOrdersToSellerListState.toMutableList()

            }

    }

    fun changeStateToAcceptedOrRejected(orderID:String,pID:String,status:OrderStatus){
        Log.i("changeStateToAccepted","inside state change")

        db.collection("orders")
            .whereEqualTo("orderId",orderID)
            .get()
            .addOnSuccessListener {
                Log.i("changeStateToAccepted",it.toString())

                var docID = it.documents.get(0).id

                if(it != null && !it.isEmpty){
                    var tempOrderList = it.documents.get(0).data as MutableMap<String,Any>
                    Log.i("changeStateToAccepted",tempOrderList.toString())
                    var tempProductList = tempOrderList.get("orderList")  as MutableList<MutableMap<String,Any>>
                    Log.i("changeStateToAccepted",tempProductList.toString())

                    for(doc in tempProductList){
                        if(doc.get("pid") ==  pID){
                            Log.i("changeStateToAccepted","before update "+doc.toString())
                            doc["status"] = status


                        }
                    }

                    db.collection("orders").document(docID)
                        .update("orderList",tempProductList)
                }



            }


    }





}