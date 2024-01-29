package com.example.gloomhavestoryline2.db.entities


data class Character(
    var id: String = "",
    var nickname: String = "",
    @field:JvmField
    var isHost: Boolean = false,
    var name: String = "",
    val money: Int = 0,
    val experience: Int = 0,
    val level: Int = 1,
    val abilities: List<Ability> = listOf(),
    val items: List<Item> = listOf()
) {
    fun getFullName(): String{
        return "$nickname - $name"
    }

    fun getFullLevel(): String{
        return "Level: $level"
    }

    fun getFullMoney(): String{
        return "$money$"
    }

    fun getFullExperience(): String{
        return "Next level: $experience / 100"
    }
}
