package ru.hse.fmcs.tickgame.models

abstract class UIState {
    class Start : UIState() {}
    class Login : UIState() {}
    class Register : UIState() {}
    class Menu : UIState() {}
    class ChooseLobby : UIState() {}
    class Quit : UIState() {}

    open class Lobby : UIState() {
        class MainLobby : Lobby() {}
        class GameField : Lobby() {}
    }
}
