package ru.hse.fmcs.tickgame.models

import ru.hse.fmcs.tickgame.data.User

abstract class AuthState {
    class Authenticated(user: User): AuthState() {
        val authenticatedUser = user
    }
    class Unauthenticated(): AuthState() {}
    class InProcess(cachedUser: User): AuthState() {
        val cachedUser = cachedUser
    }
}