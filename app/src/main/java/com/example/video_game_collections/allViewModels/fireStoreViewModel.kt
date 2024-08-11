package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.productModel
import com.google.firebase.firestore.FirebaseFirestore

val productsCollection = "products"

class fireStoreViewModel : ViewModel() {

        val db = FirebaseFirestore.getInstance()


        fun addProductsIntoDB( pname : String, pcost : Int, sellerId : String ){

            var tempProductModel = productModel(pName = pname, pCost = pcost, sellerID = sellerId)

            db.collection(productsCollection)
                .add(tempProductModel)
                .addOnSuccessListener {
                    Log.i("response","producted added by seller is added")
                }



        }



        fun displayAllProductsBySeller(){

        }



}