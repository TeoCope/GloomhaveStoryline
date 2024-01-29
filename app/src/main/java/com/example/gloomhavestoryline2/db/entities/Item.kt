package com.example.gloomhavestoryline2.db.entities


data class Item(
    val name: String = "",
    val number: Int = 0,
    val effect: String = "",
    val cost: Int = 0,
    val avail: Int = 0,
    val slot: String = ""
) {
    fun getFullName(): String{
        return "#$number $name"
    }

    fun getPrice(): String{
        return "$cost$"
    }

    fun getHalfPrice(): String{
        return "${cost/2}$"
    }

    fun getAvailNumber(): String{
        return "2 / $avail"
    }
}
