package com.example.katundu.ui;

public class ControladoraChat {

    private static String username1 = "nom usuari loguejat";
    private static String username2 = "nom usuari amb qui parla l'usuari loguejat";
    private static String id_Chat = "id Chat";

    public static String getUsername1() {
        return username1;
    }

    public static void setUsername1(String username1) {
        ControladoraChat.username1 = username1;
    }

    public static String getUsername2() {
        return username2;
    }

    public static void setUsername2(String username2) {
        ControladoraChat.username2 = username2;
    }

    public static String getId_Chat() {
        return id_Chat;
    }

    public static void setId_Chat(String id_Chat) {
        ControladoraChat.id_Chat = id_Chat;
    }
}
