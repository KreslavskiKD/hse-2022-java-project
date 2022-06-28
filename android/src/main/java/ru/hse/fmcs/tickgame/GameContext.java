package ru.hse.fmcs.tickgame;

public class GameContext {
    private static String login;
    private static String serverAddress = "194.87.95.226";
    private static int roomPort = 6433;
    private static int loginPort = 6433;

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
