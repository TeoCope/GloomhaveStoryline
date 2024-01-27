package com.example.gloomhavestoryline2.db.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Game(
    val id: String = "",
    val squadName: String = "",
    var currentMission: Int = 0,
    val squad: List<Character> = listOf(),
    val items: List<Item> = listOf(),
    val missions: List<Mission> = listOf(),
    val charactersAvailable: MutableList<String> = mutableListOf("Demolitionist","Hatchet","Voidwarden","Red Guard")
) {
    fun getSquadMembers(): String{
        var newString = "Squad: "
        for (member in squad) {
            newString = newString + member.nickname + " "
        }
        return newString
    }
}
