package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.productModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val productsCollection = "products"

class fireStoreViewModel : ViewModel() {

        val db = FirebaseFirestore.getInstance()
        var auth = FirebaseAuth.getInstance()


        private var _allProductsBySellerState = MutableLiveData<MutableList<productModel>>()
        var allProductsBySellerState : LiveData<MutableList<productModel>> = _allProductsBySellerState



    init {
        displayAllProductsBySeller()
    }

    fun addProductsIntoDB( pname : String, pcost : Int, sellerId : String ){

            var tempProductModel = productModel(pName = pname, pCost = pcost, sellerID = sellerId)

            db.collection(productsCollection)
                .add(tempProductModel)
                .addOnSuccessListener {
                    Log.i("response","producted added by seller is added")
                }



        }


        var tempProductsList  = mutableListOf<productModel>()
        fun displayAllProductsBySeller(){
            db.collection(productsCollection)
                .addSnapshotListener { value, error ->

                    if (value != null) {
                        tempProductsList.clear()

                        for(it in value){

                                if(it["sellerID"].toString() == auth.currentUser?.uid.toString()){
                                    var tempProductModel = productModel(
                                        pCost = it["pcost"].toString().toInt(),
                                        pName = it["pname"].toString(),
                                        sellerID = it["sellerID"].toString()
                                    )

                                    tempProductsList.add(tempProductModel)
                                }


                        }
                    }

                    _allProductsBySellerState.value = tempProductsList.toMutableList()

                }

        }



}