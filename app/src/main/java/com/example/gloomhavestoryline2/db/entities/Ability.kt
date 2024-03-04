package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Ability(
    val name: String = "",
    val level: Int = 0,
    val image: String = ""
) {
    fun getFullLevel() : String{
        return "Level: $level"
    }
}
