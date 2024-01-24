package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val email: String = "",
    val nickname: String = "",
    val games: List<String> = listOf()
) : Parcelable
