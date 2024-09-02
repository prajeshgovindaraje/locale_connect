package com.example.video_game_collections.allViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.video_game_collections.dataModels.OrderStatus
import com.example.video_game_collections.dataModels.productOrderModel
import com.example.video_game_collections.helperFunctions.generateRandomID
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ordersCustomerSideViewModel : ViewModel(){

    var db = FirebaseFirestore.getInstance()

    //track total cost of all the ordered products
    private var _totalCost = MutableLiveData<Double>(0.0)
    var totalCost : LiveData<Double> = _totalCost

    //track Order Picke Up Time
    private var _formattedPickUpTimeState = MutableLiveData<String>("nothing")
    var formattedPickUpTimeState : LiveData<String> = _formattedPickUpTimeState

    //list to store all the added products
    private var _productsInCartState = MutableLiveData<MutableList<productOrderModel>>()
    var productsInCartState : LiveData<MutableList<productOrderModel>> = _productsInCartState


    //Map to increase and decrease count of individual items
    private var _productsCountMapState = MutableLiveData<MutableMap<String,Int>>()
    var productsCountMapState : LiveData<MutableMap<String,Int>> = _productsCountMapState

    //Represents Orders stored in DB
    private var _ordersMapState = MutableLiveData<MutableList<Map<String,Any>>>()
    var ordersMapState : LiveData<MutableList<Map<String,Any>>> = _ordersMapState

    //used to display current products in a order from order page
    private var _displayProductsInCurrentOrderListState = MutableLiveData<MutableList<Map<String,Any>>>()
    var displayProductsInCurrentOrderListState : LiveData<MutableList<Map<String,Any>>> = _displayProductsInCurrentOrderListState


    var tempProductsInCartList = mutableListOf<productOrderModel>() //an empty list
    fun addOrUpdateToProductsInCartList(productInCart : productOrderModel){

        Log.i("orderIncDec","inside addorUpdate cart list function")

        if(tempProductsInCartList.isEmpty()){
            tempProductsInCartList.add(productInCart)
            Log.i("orderIncDec","list empty so product added")
        }else if(tempProductsInCartList.contains(productInCart)){
            var index = tempProductsInCartList.indexOf(productInCart)
            tempProductsInCartList.set(index, productInCart)
            Log.i("orderIncDec","same name product present to list updated")
        }else{
            tempProductsInCartList.add(productInCart)
            Log.i("orderIncDec","new product so added")
        }

        Log.i("orderIncDec","the list is ${tempProductsInCartList}")

        _productsInCartState.value = tempProductsInCartList.toMutableList()

    }


    fun removeProductsInCartList(productInCart: productOrderModel){
        tempProductsInCartList.remove(productInCart)

        _productsInCartState.value = tempProductsInCartList.toMutableList()

    }



    var tempProductsCountMap = mutableMapOf<String,Int>()
    fun incrementOrderCount(pName : String, currCount : Int ){

        tempProductsCountMap.set(pName,currCount+1)

        _productsCountMapState.value = tempProductsCountMap.toMutableMap()

    }



    fun decrementCount(pName : String, currCount : Int ){

        tempProductsCountMap.set(pName,currCount-1)

        _productsCountMapState.value = tempProductsCountMap.toMutableMap()

    }

    fun getCurrCount(pName : String): Int{

        return tempProductsCountMap.get(pName) ?: 0

    }


    fun addToTotalCost(pCost : Double){
        _totalCost.value = _totalCost.value?.plus(pCost)
    }

    fun reduceFromTotalCost(pCost : Double){
        _totalCost.value = _totalCost.value?.minus(pCost)
    }

    fun setFormattedPickUpTime(formattedPickUpTime:String){
        _formattedPickUpTimeState.value = formattedPickUpTime
    }
    fun getFormattedPickUpTime():String{
        return formattedPickUpTimeState.value ?: "nothing retreived from getPickUpTime"
    }





    //used while checkout button is clicked
    fun addIntoOrders(
        orderList: MutableList<productOrderModel>,
        totalCost: Double,
        context: Context,
        buyerID : String,
        status : String,
        sellerID : String,
        pickUpTime: Timestamp,

        isCancelledBySeller : Boolean,
        isCancelledByCustomer : Boolean,

        isRemovedBySeller : Boolean,
        isRemovedByCustomer : Boolean,


    ){

        val currentTime = Timestamp(Date())
        val orderID = generateRandomID().generateRandomAlphanumericString(20)

        val orderDocument = mapOf(
            "orderList" to orderList,  // Store the list as a field
            "totalOrderCost" to totalCost,
            "buyerID" to buyerID,
            "timestamp" to currentTime,
            "orderId" to orderID,
            "sellerID" to sellerID,
            "status" to status,
            "pickUpTime" to pickUpTime,

            "isCancelledBySeller" to isCancelledBySeller,
            "isCancelledByCustomer" to isCancelledByCustomer,

            "isRemovedBySeller" to isRemovedBySeller,
            "isRemovedByCustomer" to isRemovedByCustomer
        )

        db.collection("orders").add(orderDocument)
            .addOnSuccessListener {
               // Log.i("orderProd",_ordersMapState.value.toString())
                Toast.makeText(context,"Order Placed",Toast.LENGTH_LONG).show()
                tempProductsInCartList.clear()
                _productsInCartState.value = tempProductsInCartList.toMutableList()
                _totalCost.value = 0.0
                _formattedPickUpTimeState.value = "nothing"
                tempProductsCountMap.clear()
                _productsCountMapState.value = tempProductsCountMap.toMutableMap()


            }
            .addOnFailureListener {
                Toast.makeText(context,it.message,Toast.LENGTH_LONG).show()
            }

    }


    var tempOrderedProductMap = mutableListOf<Map<String,Any>>()
    //display customer order
    fun displayCurrentUserOrders(
        userID : String
    ){
        db.collection("orders")
            .whereEqualTo("buyerID",userID)
            .addSnapshotListener { value, error ->
                tempOrderedProductMap.clear()

                Log.i("orderProd","fetch  success")


                if (value != null) {
                    for (doc in value){


                       if( doc.get("isRemovedByCustomer") == false){
                           tempOrderedProductMap += doc.data as Map<String,Any>

                           Log.i("orderProd","tempOrderedProductMap update  success")

                       }



                    }
                }
                _ordersMapState.value = tempOrderedProductMap.toMutableList()


            }

    }



    fun deleteEntireOrder(
        orderID: String

    ){

        db.collection("orders")
            .whereEqualTo("orderId",orderID)
            .get()
            .addOnSuccessListener {

                if(it != null && !it.isEmpty){

                    tempOrderedProductMap.remove(it.documents[0].data)
                    var docID = it.documents[0].id
                    db.collection("orders").document(docID).delete()
                        .addOnSuccessListener {
                            Log.i("deleteOrder","before delete"+_ordersMapState.value.toString())

                            _ordersMapState.value = tempOrderedProductMap.toMutableList()
                            Log.i("deleteOrder","after delete"+_ordersMapState.value.toString())

                        }
                }

            }


    }




    var tempDisplayProductsInCurrentOrderList = mutableListOf<Map<String,Any>>()
    fun displayProductsInCurrentOrder(orderedList : MutableList<Map<String,Any>>){
        Log.i("productState","inside display products")
        db.collection("orders")
            .whereEqualTo("orderList",orderedList)
            .addSnapshotListener { value, error ->
                tempDisplayProductsInCurrentOrderList.clear()

                if(value != null && !value.isEmpty){
                    var tempOrderMap = value.documents.get(0).data as MutableMap<String, Any>
                    var tempProductList = tempOrderMap["orderList"]  as MutableList<MutableMap<String, Any>>
                    tempDisplayProductsInCurrentOrderList += tempProductList

                    Log.i("productState","display products "+_displayProductsInCurrentOrderListState.value.toString())
                    _displayProductsInCurrentOrderListState.value = tempDisplayProductsInCurrentOrderList.toMutableList()

                }



            }






    }


    //this counts and returns the no. of PENDING,ACCEPTED,RJECTED products in a order currently
    fun countStatus(
        productsInOrderList:List<Map<String,Any>>,
        onSuccess:(MutableList<Int>) -> Unit
    ){
        var totalProductsInTheOrder = productsInOrderList.size

        var countList = mutableListOf(0,0,0)


        for (product in productsInOrderList) {
            when (product["status"]) {

                OrderStatus.PENDING.toString() -> countList[0] = countList[0] + 1
                OrderStatus.ACCEPTED.toString() -> countList[1] = countList[1] + 1
                OrderStatus.REJECTED.toString() -> countList[2] = countList[2] + 1
            }
        }



        onSuccess(countList)
    }


    fun changeStateToAcceptedOrRejected(
        userID: String,
        orderID: String,
        pID: String,
        status: OrderStatus,
        context: Context,
        productList : MutableList<Map<String,Any>>
    ){
        Log.i("changeStateToAccepted","inside state change")

        db.collection("orders")
            .whereEqualTo("orderId",orderID)
            .get()
            .addOnSuccessListener {




                if(it != null && !it.isEmpty){
                    var docID = it.documents.get(0).id

                    var tempOrderMap = it.documents.get(0).data  as MutableMap<String, Any>
                  //  Log.i("changeStateToAccepted","map = "+tempOrderMap.toString())
                    var tempProductList = tempOrderMap["orderList"]  as MutableList<MutableMap<String, Any>>
                    Log.i("changeStateToAccepted","before update list = "+tempProductList.toString())

                    for (doc in tempProductList){
                        if(doc.get("pid") == pID){
                            doc["status"] = status.name
                        }
                    }
                    Log.i("changeStateToAccepted","after update list = "+tempProductList.toString())


                    // Update the document in Firestore with the properly formatted map


                    db.collection("orders").document(docID)
                        .update("orderList",tempProductList)
                        .addOnSuccessListener {
                            Log.i("changeStateToAccepted","update success")
                            displayProductsInCurrentOrder(tempProductList as MutableList<Map<String,Any>>)
                           // displayCurrentUserOrders(userID)


                        }
                        .addOnFailureListener {
                            Log.i("changeStateToAccepted",it.message.toString())
                        }

                }else{
                    Toast.makeText(context,"The Order has been cancelled by the user. Go back.",Toast.LENGTH_LONG).show()

                }



            }
            .addOnFailureListener {
                    Toast.makeText(context,"Network Error.",Toast.LENGTH_LONG).show()
            }


    }



    fun makeCustomerRemoveTrue(orderId:String){

        db.collection("orders")
            .whereEqualTo("orderId",orderId)
            .get()
            .addOnSuccessListener {

                val id  = it.documents.get(0).id

                db.collection("orders").document(id)
                    .update("isRemovedByCustomer",true)

            }



    }

    fun makeCustomerCancelTrue(orderId:String){

        db.collection("orders")
            .whereEqualTo("orderId",orderId)
            .get()
            .addOnSuccessListener {

                val id  = it.documents.get(0).id

                db.collection("orders").document(id)
                    .update("isCancelledByCustomer",true)

            }



    }


    fun deleteIndovidualProduct( pid:String,orderId:String){


        db.collection("orders")
            .whereEqualTo("orderId",orderId)
            .get()
            .addOnSuccessListener { //get the document

                if(!it.isEmpty && it != null){
                    val docID = it.documents.get(0).id

                    db.collection("orders").document(docID)
                        .get()
                        .addOnSuccessListener {//from the only document that is matched
                            val buyerId = it.get("buyerID")
                            var  tempProductList:MutableList<Map<String,Any>> = it.get("orderList") as MutableList<Map<String,Any>>
                            if(tempProductList.size == 1){
                                makeCustomerCancelTrue(it.get("orderId").toString())
                                makeCustomerRemoveTrue(it.get("orderId").toString())
                            }

                            val indexOfCurrentProduct = tempProductList.indexOfLast {
                                it["pid"] == pid
                            }

                            if (indexOfCurrentProduct != -1) {
                                Log.i("IndexFound", "Product index: $indexOfCurrentProduct")
                                var tempCost = it.get("totalOrderCost").toString().toDouble() - tempProductList.get(indexOfCurrentProduct).get("totalProductCost").toString().toDouble()



                                tempProductList.removeAt(indexOfCurrentProduct)

                                db.collection("orders").document(docID)
                                    .update("orderList",tempProductList)
                                    .addOnSuccessListener {
                                        db.collection("orders").document(docID)
                                            .update("totalOrderCost",tempCost)
                                            .addOnSuccessListener {
                                                 displayProductsInCurrentOrder(tempProductList)
                                                displayCurrentUserOrders(buyerId.toString())

                                            }

                                    }




                            } else {
                                // Product not found
                                Log.i("IndexNotFound", "Product with pid $pid not found in orderList")
                            }


                        }

                }


            }


    }
















}