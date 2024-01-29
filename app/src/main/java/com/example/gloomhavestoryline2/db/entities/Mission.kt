package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Mission(
    val name: String = "",
    val number: Int = 0,
    @field:JvmField
    var isCompleted: Boolean = false
) {
    fun getMissionName(): String{
        return "#$number $name"
    }
}
