package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val name: String = "",
    val number: Int = 0,
    val effect: String = "",
    val cost: Int = 0,
    val avail: Int = 0,
    val slot: String = ""
) : Parcelable
