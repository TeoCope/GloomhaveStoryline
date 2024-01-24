package com.example.gloomhavestoryline2.db.entities

data class Game(
    val id: String = "",
    val squadName: String = "",
    val playerNumber: Int = 0,
    val ready: Boolean = false,
    val currentMission: Int = 0,
    val squad: Map<String, Character> = mapOf(),
)
