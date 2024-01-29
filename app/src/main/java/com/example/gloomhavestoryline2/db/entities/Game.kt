package com.example.gloomhavestoryline2.db.entities

data class Game(
    val id: String = "",
    val squadName: String = "",
    var currentMission: Int = 0,
    var squadMembers: List<String> = listOf(),
    val charactersAvailable: MutableList<String> = mutableListOf("Demolitionist","Hatchet","Voidwarden","Red Guard"),
    @field:JvmField
    val isEnd: Boolean = false
) {
    fun getFullSquadMembers() :String {
        var result = ""
        for (member in squadMembers) {
            result += " - $member"
        }
        return result
    }
}
