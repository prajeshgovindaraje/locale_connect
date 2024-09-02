package com.example.video_game_collections.Screens.CustomerScreens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.video_game_collections.allViewModels.UI_ViewModel
import com.example.video_game_collections.allViewModels.fireBaseAuthViewModel
import com.example.video_game_collections.allViewModels.ordersCustomerSideViewModel
import com.example.video_game_collections.dataModels.productOrderModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun addToCartScreen(
    ordersCustomerSideViewModel: ordersCustomerSideViewModel,
    fireBaseAuthViewModel: fireBaseAuthViewModel,
    uiViewmodel: UI_ViewModel,
    navController: NavController,

) {

    val observedDatePickerDialogueState = uiViewmodel.datePickerDialogState.observeAsState()

    val context = LocalContext.current
    val buyerID = fireBaseAuthViewModel.auth.currentUser?.uid

    val observedProductsInCartState = ordersCustomerSideViewModel.productsInCartState.observeAsState(emptyList<productOrderModel>())
    val observedTotalCost = ordersCustomerSideViewModel.totalCost.observeAsState(0.0)
    val observedPickUpTime = ordersCustomerSideViewModel.formattedPickUpTimeState.observeAsState()


    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(observedProductsInCartState.value.isEmpty()){
                //Spacer(modifier = Modifier.weight(1f))
                Text(text = "NO CURRENT PRODUCTS ARE SELECTED",
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxHeight(0.7f)
                    )
            }

            else{
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .fillMaxWidth()
                ) {



                    items(observedProductsInCartState.value){

                        Box(modifier = Modifier.fillMaxWidth(0.9f)){

                            Row {
                                AsyncImage(
                                    model = it.imageURL,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .height(120.dp)
                                        .border(2.dp, Color.DarkGray),
                                    contentScale = ContentScale.Crop
                                )

                                Column {
                                    Text(text = "Name: ${it.pName}")


                                    Text(text = "cost: ${it.pCost}")
                                    Text(text = "quantity: ${it.quantity}")
                                    Text(text = "totalCost: ${it.totalProductCost}")


                                }

                            }

                        }

                    }

                }
            }



            Row(

                modifier = Modifier
                    .fillMaxWidth(),
                  //  .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceAround,

            ) {
                //assigned when Date Dialogue is shown
                //timstamp value that will be stored in firstore
                var pickUpTime:Timestamp? by remember {
                    mutableStateOf(null)
                }

                //chosen timestamp value is formatted nicely to display to the user
                //this cuts the unneeded details like timezone, nanosecs etc


                Column {
                    Text(text = "pick up time is:\n ${ordersCustomerSideViewModel.getFormattedPickUpTime()}")

                    Button(onClick = {
                        Log.i("dateTime","button clicker")

                        Log.i("dateTime","state date :"+observedDatePickerDialogueState.value)

                        //make Date dialogue to show
                        uiViewmodel.makeDatePickerDialogVisible()
                    },
                    ) {
                        Text(text = "Choose Pick up Time")

                    }
                }

               // Log.i("dateTime","state date :"+observedDatePickerDialogueState.value)

                if(observedDatePickerDialogueState.value == true){

                    //this funtion displays the dateTime Picker dialgue
                    //it returns the timestamp along with its formatted form as callback
                    //then these two are assigned to their respective rememberVariables for recomposition
                     getTimeFromUser(
                        uiViewmodel = uiViewmodel,
                        context = context
                    ){timestamp, formattedDate ->
                        pickUpTime = timestamp
                         ordersCustomerSideViewModel.setFormattedPickUpTime(formattedDate)

                         Log.i("dateTime","picked time is ${formattedDate}")

                     } //call the function which return the date when it is true

                }


                Button(onClick = {



                    //add into orders collection
                    if (buyerID != null) {

                        if(observedProductsInCartState.value.isNotEmpty()){
                            var sellerID = observedProductsInCartState.value.get(0).sellerID

                            if(pickUpTime != null){
                                ordersCustomerSideViewModel.addIntoOrders(


                                    observedProductsInCartState.value.toMutableList(),
                                    context = context,
                                    totalCost = observedTotalCost.value,
                                    buyerID = buyerID,
                                    status = "pending",
                                    sellerID = sellerID,
                                    pickUpTime = pickUpTime!!,
                                    isCancelledBySeller = false,
                                    isCancelledByCustomer = false,

                                    isRemovedBySeller = false,
                                    isRemovedByCustomer = false




                                )
                            }else{
                                Toast.makeText(context,"Select time up time first",Toast.LENGTH_LONG).show()

                            }

                        }else{
                            Toast.makeText(context,"NO PRODUCTS ARE SELECTED TO ADD",Toast.LENGTH_LONG).show()
                        }



                    }


                },
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(text = "Place Order\nTotal cost ${observedTotalCost.value}")
                }


            }


        }

    }



}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun getTimeFromUser(
    uiViewmodel: UI_ViewModel,
    context: Context,
    onSuccess:(Timestamp?,String) ->Unit

) {

    Log.i("dateTime","get Time from User called")

    var selectedDate = ""
    var selectedTime = ""

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            selectedTime = "$hourOfDay:$minute"
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
    )

    var timeStamp:Timestamp? = null

    datePickerDialog.show()

    datePickerDialog.setOnDismissListener {

        if (selectedDate.isNotEmpty()) {

            timePickerDialog.show()


        }else{
            selectedDate =""
            uiViewmodel.makeDatePickerDialogVHidden()
        }
    }

    timePickerDialog.setOnDismissListener{
        if(selectedTime.isEmpty()){
            selectedTime = ""
            selectedDate = ""
            uiViewmodel.makeDatePickerDialogVHidden()
        }else{

            Log.i("dateTime" ,"date = ${selectedDate}, time = ${selectedTime}")
            // Convert the selected date and time into a Timestamp
            val dateTime = "$selectedDate $selectedTime"
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val parsedDate: Date = dateFormat.parse(dateTime)
            val currDateTime = Date()

            if(parsedDate.before(currDateTime)){
                Toast.makeText(context,"INVALID DATE AND TIME",Toast.LENGTH_LONG).show()
            }else{
                val outputDateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.getDefault())
                val formattedDate = outputDateFormat.format(parsedDate)

                // Convert to Timestamp
                timeStamp = Timestamp(parsedDate)
                Log.i("dateTime","parresd : "+timeStamp)
                onSuccess(timeStamp,formattedDate)
            }


            uiViewmodel.makeDatePickerDialogVHidden()
        }
    }




}