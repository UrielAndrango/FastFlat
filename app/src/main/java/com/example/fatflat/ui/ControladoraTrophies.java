package com.example.fatflat.ui;

public class ControladoraTrophies {
    //private static Boolean[] trofeos_usuario = new Boolean[]{true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
    private static Boolean[] trofeos_usuario = new Boolean[]{false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
    private static String username;

    public static Boolean[] getTrofeos_usuario() {
        return trofeos_usuario;
    }

    public static void setTrofeos_usuario(Boolean[] trofeos_usuario) {
        ControladoraTrophies.trofeos_usuario = trofeos_usuario;
    }

    public static Boolean TrofeoConseguido(int pos) {
        //pos empieza en 0
        return trofeos_usuario[pos];
    }

    public static void DesbloquearTrofeo(int pos) {
        //pos empieza en 0
        trofeos_usuario[pos] = true;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ControladoraTrophies.username = username;
    }
}
