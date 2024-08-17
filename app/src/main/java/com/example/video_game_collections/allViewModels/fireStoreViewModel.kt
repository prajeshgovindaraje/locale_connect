package com.example.video_game_collections.allViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.productModel
import com.example.video_game_collections.dataModels.shopCardModel
import com.example.video_game_collections.dataModels.usersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

val productsCollection = "products"

class fireStoreViewModel : ViewModel() {

        val db = FirebaseFirestore.getInstance()
        var auth = FirebaseAuth.getInstance()


        private var _allProductsBySellerState = MutableLiveData<MutableList<productModel>>()
        var allProductsBySellerState : LiveData<MutableList<productModel>> = _allProductsBySellerState

    private var _allProductsForCustomerState = MutableLiveData<MutableList<productModel>>()
    var allProductsForCustomerState : LiveData<MutableList<productModel>> = _allProductsForCustomerState

    private var _allShopsForCustomerState = MutableLiveData<MutableList<shopCardModel>>()
    var allShopsForCustomerState :LiveData<MutableList<shopCardModel>> = _allShopsForCustomerState



    init {
       // displayAllProductsBySeller()

    }


    fun updateUserModelWithlocation(userID : String,locationList : List<Double>){
        db.collection("users").whereEqualTo("userID",userID)
            .get()
            .addOnSuccessListener {
                for(document in it){
                    db.collection("users").document(document.id)
                        .update(
                            "location",locationList
                        )
                }
            }
    }


    fun addProductsIntoDB( pname : String, pcost : Double, sellerId : String, imageURL : String? ){

            var tempProductModel = productModel(
                pName = pname,
                pCost = pcost,
                sellerID = sellerId,
                imageURL = imageURL
            )

            db.collection(productsCollection)
                .add(tempProductModel)
                .addOnSuccessListener {
                    Log.i("response","producted added by seller is added")
                }
                .addOnFailureListener {

                    Log.i("response","${it.message}")
                }



        }


        var tempProductsListBySeller  = mutableListOf<productModel>()
        fun displayAllProductsBySeller(userID : String){

            Log.i("response","inside display product by seller dfun ${userID}")

            db.collection(productsCollection)
                .whereEqualTo("sellerID",userID)
                .addSnapshotListener { value, error ->
                    Log.i("response","inside addSnap display product by seller dfun ${userID}")

                    if (value != null) {
                        tempProductsListBySeller.clear()

                        for(it in value){

                                    var tempProductModel = productModel(
                                        pCost = it["pcost"].toString().toDouble(),
                                        pName = it["pname"].toString(),
                                        sellerID = it["sellerID"].toString(),
                                        imageURL = it["imageURL"].toString()
                                    )

                                    tempProductsListBySeller.add(tempProductModel)



                        }
                    }

                    _allProductsBySellerState.value = tempProductsListBySeller.toMutableList()

                }

        }



    lateinit  var  location : List<Double>

    var tempProductsListforCustomer  = mutableListOf<productModel>()
    fun displayAllProductsforCustomer(userId : String){

        //this way is inefficient to fetch location of Curruser as docId and UserId are same
//        db.collection("users")
//            .whereEqualTo("userID",userId)
//            .addSnapshotListener { value, error ->
//
//                if (value != null) {
//                    for(it in value){
//                            location = it["location"] as List<Double>
//                        Log.i("productsforcustomer","cus location :" + location.toString())
//
//                    }
//                }
//
//        }

        //so this method is used to fetch user location
        db.collection("users").document(userId).addSnapshotListener { value, error ->
            location = value?.get("location") as List<Double>
            Log.i("response","${location}")
        }


        db.collection("users")
            .whereEqualTo("role","seller")
            .addSnapshotListener { value, error ->

                if (value != null) {
                    tempProductsListforCustomer.clear()



                    for(it in value){
                       // Log.i("productsforcustomer",it["email"].toString())


                        //locatin of the seller
                        var tempLocation = it["location"] as List<Double>


                        //checking customer and seller location
                        if(location[0].roundToInt() == tempLocation[0].roundToInt() && location[1].roundToInt() == tempLocation[1].roundToInt()){


                            db.collection(productsCollection)
                                .whereEqualTo("sellerID",it["userID"])
                                .addSnapshotListener { value, error ->

                                    Log.i("productsforcustomer",it["email"].toString()+" location: "+tempLocation.toString())

                                    if (value != null) {
                                        for(it in value){

                                            tempProductsListforCustomer.add(
                                                productModel(
                                                    pName = it["pname"].toString(),
                                                    pCost = it["pcost"].toString().toDouble(),
                                                    sellerID = it["sellerID"].toString(),
                                                    imageURL = it["imageURL"].toString()
                                                )
                                            )
                                            _allProductsForCustomerState.value = tempProductsListforCustomer.toMutableList()



                                        }
                                    }

                                }


                        }




                    }

                }


            }

    }




    var tempShopsListforCustomer  = mutableListOf<shopCardModel>()

    fun displayAllShopsForCustomer(userId: String){


        db.collection("users").document(userId).addSnapshotListener { value, error ->
            location = value?.get("location") as List<Double>
            Log.i("response","${location}")
        }


        db.collection("users")
            .whereEqualTo("role","seller")
            .addSnapshotListener { sellers, error ->

                if (sellers != null) {
                    tempShopsListforCustomer.clear()



                    for(seller in sellers){
                        // Log.i("productsforcustomer",it["email"].toString())


                        //locatin of the seller
                        var tempLocation = seller["location"] as List<Double>


                        //checking customer and seller location
                        if(location[0].roundToInt() == tempLocation[0].roundToInt() && location[1].roundToInt() == tempLocation[1].roundToInt()){

                            var tempShops = shopCardModel(
                                shopName = seller["shopName"].toString(),
                                shopType = seller["shopType"].toString(),
                                userID = seller["userID"].toString(),
                                shopImage = seller["shopImage"].toString()

                            )

                            tempShopsListforCustomer.add(tempShops)


                        }

                        _allShopsForCustomerState.value = tempShopsListforCustomer.toMutableList()


                    }

                }


            }

    }







}