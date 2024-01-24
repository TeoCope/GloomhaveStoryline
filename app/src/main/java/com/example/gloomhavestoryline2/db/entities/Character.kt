package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    val nickname: String = "",
    val name: String = "",
    val money: Int = 0,
    val experience: Int = 0,
    val level: Int = 1,
    val abilities: List<Ability> = listOf(),
    val items: List<Item> = listOf()
) : Parcelable
