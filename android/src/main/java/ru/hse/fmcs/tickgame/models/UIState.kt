package ru.hse.fmcs.tickgame.models

abstract class UIState {
    class Start : UIState() {}
    class Login : UIState() {}
    class Register : UIState() {}
    class Menu : UIState() {}
    class Quit : UIState() {}
}
