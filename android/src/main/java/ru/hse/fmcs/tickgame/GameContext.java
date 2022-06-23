package ru.hse.fmcs.tickgame;

public class GameContext {
    private static String login;
    private static String serverAddress = "192.168.0.104";
    private static int roomPort = 8080;
    private static int loginPort = 8080;

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getRoomPort() {
        return roomPort;
    }

    public static String getLogin() {
        return login;
    }

    public static int getLoginPort() {
        return loginPort;
    }

    public static void setLogin(String login) {
        GameContext.login = login;
    }

    public static void setServerAddress(String address) {
        serverAddress = address;
    }
}
