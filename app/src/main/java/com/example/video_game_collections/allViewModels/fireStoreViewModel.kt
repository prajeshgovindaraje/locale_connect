package com.example.video_game_collections.allViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
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
                        .addOnSuccessListener {

                            //displaying available shops for user only after getting the location
                            Log.i("productsforcustomer","dispoay called")
                            displayAllShopsForCustomer(userID)
                        }
                }
            }
    }



    fun deleteFromdb(pName:String,context:Context, sellerID:String){


        db.collection(productsCollection)
            .whereEqualTo("sellerID",sellerID)
            .whereEqualTo("pname",pName)
            .addSnapshotListener { value, error ->

                //for loop is avoided because only one item can match the give whereQuery
                if (value != null && !value.isEmpty) {
                    val documentID = value.documents[0].id
                    db.collection(productsCollection).document(documentID).delete()
                }


            }


    }



    fun addProductsIntoDB(
        pname: String,
        pcost: Double,
        sellerId: String,
        imageURL: String?,
        context: Context,
        pID : String
    ){
        // check for duplicate product names
        Log.i("duplicateProduct","runs")
        var isDuplicatePresent = false
            db.collection(productsCollection)
                .whereEqualTo("sellerID",sellerId)
                .get()
                .addOnSuccessListener {products ->


                    if (products != null) {
                        for(product  in products){
                            Log.i("duplicateProduct","${product["pname"]} - ${pname}")
                            if(product["pname"].toString() == pname){
                                isDuplicatePresent = true
                                Toast.makeText(context,"product added failed",Toast.LENGTH_LONG).show()




                            }

                        }


                        if(isDuplicatePresent == false){
                            var tempProductModel = productModel(
                                pName = pname,
                                pCost = pcost,
                                sellerID = sellerId,
                                imageURL = imageURL,
                                pID = pID
                            )

                            db.collection(productsCollection)
                                .add(tempProductModel)
                                .addOnSuccessListener {
                                    Log.i("duplicateProduct","producted added by seller is added")
                                    Toast.makeText(context,"product  added",Toast.LENGTH_LONG).show()

                                }
                                .addOnFailureListener {

                                    Log.i("duplicateProduct","${it.message}")
                                }
                        }








                    }
                }













        }


    var tempProductsListBySeller = mutableListOf<productModel>()

    fun displayAllProductsBySeller(userID: String) {
        Log.i("response", "inside display product by seller function ${userID}")

        db.collection(productsCollection)
            .whereEqualTo("sellerID", userID)
            .addSnapshotListener { value, error ->
                Log.i("response", "inside addSnapshotListener display product by seller function ${userID}")

                if (value != null) {
                    tempProductsListBySeller.clear()

                    for (it in value) {
                        val tempProductModel = productModel(
                            pCost = it["pcost"].toString().toDouble(),
                            pName = it["pname"].toString(),
                            sellerID = it["sellerID"].toString(),
                            imageURL = it["imageURL"].toString(),
                            pID = it["pid"].toString()
                        )

                        Log.i("response ",tempProductModel.pID)

                        tempProductsListBySeller.add(tempProductModel)
                    }
                }

                _allProductsBySellerState.value = tempProductsListBySeller.toMutableList()
            }
    }

    fun changeDisplayAllProductsBySellerOrder(pID: String) {
        Log.i("changeOrder", "inside change function")

        // Find the index of the product with the given pID
        val index = tempProductsListBySeller.indexOfFirst { it.pID == pID }
        Log.i("changeOrder", "Index of product with pID $pID: $index")

        if (index != -1) {
            // If the product is found, remove it from its current position
            val product = tempProductsListBySeller.removeAt(index)
            Log.i("changeOrder", "Product removed: $product")

            // Add the product to the 0th index
            tempProductsListBySeller.add(0, product)
            Log.i("changeOrder", "Product moved to 0th index: $product")
        } else {
            Log.e("changeOrder", "Product with pID $pID not found!")
        }

        // Update the state with the modified list
        _allProductsBySellerState.value = tempProductsListBySeller.toMutableList()

        Log.i("changeOrder", "Updated state with new order: $tempProductsListBySeller")
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
                                                    imageURL = it["imageURL"].toString(),
                                                    pID =  it["pID"].toString()
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
            Log.i("productsforcustomer","location: ${location}")
        }


        db.collection("users")
            .whereEqualTo("role","seller")
            .addSnapshotListener { sellers, error ->

                if (sellers != null) {
                    tempShopsListforCustomer.clear()



                    for(seller in sellers){
                         Log.i("productsforcustomer",seller["email"].toString())


                        //locatin of the seller
                        var tempLocation = seller["location"] as List<Double>


                        //checking customer and seller location
                        if(tempLocation.isNotEmpty()){
                            if(location[0].roundToInt() == tempLocation[0].roundToInt() && location[1].roundToInt() == tempLocation[1].roundToInt()){

                                var tempShops = shopCardModel(
                                    shopName = seller["shopName"].toString(),
                                    shopType = seller["shopType"].toString(),
                                    userID = seller["userID"].toString(),
                                    shopImage = seller["shopImage"].toString()

                                )

                                tempShopsListforCustomer.add(tempShops)


                            }
                        }


                        Log.i("productsforcustomer","list : "+tempShopsListforCustomer.toString())
                        _allShopsForCustomerState.value = tempShopsListforCustomer.toMutableList()


                    }

                }


            }

    }







}