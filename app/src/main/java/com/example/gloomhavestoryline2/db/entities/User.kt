package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val nickname: String = "",
    val games: List<String> = listOf(),
    val completedGames: Int = 0,
    val matchInProgress: Int = 0,
    val registrationDate: Date = Calendar.getInstance().time,
) : Parcelable {
    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        val formattedData: String = dateFormat.format(registrationDate)

        return formattedData
    }
}
