package ru.hse.fmcs.tickgame.data

data class User(var login: String, var password: String) { // some other stats will be added later
}

val noUser = User("","")
