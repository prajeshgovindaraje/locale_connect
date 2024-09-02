package com.example.video_game_collections.helperFunctions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun timestampToReadable(timestamp: Timestamp):String{

    val date = timestamp.toDate()
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy h:mm a", Locale.getDefault())

    val formattedDate:String = outputDateFormat.format(date)

    return formattedDate


}