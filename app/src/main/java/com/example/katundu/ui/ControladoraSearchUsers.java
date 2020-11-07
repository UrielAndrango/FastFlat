package com.example.katundu.ui;

public class ControladoraSearchUsers {
    private static String username = "username";
    private static String nombre_real = "Nombre Real";
    private static double valoracion = 4.5;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ControladoraSearchUsers.username = username;
    }

    public static String getNombre_real() {
        return nombre_real;
    }

    public static void setNombre_real(String nombre_real) {
        ControladoraSearchUsers.nombre_real = nombre_real;
    }

    public static double getValoracion() {
        return valoracion;
    }

    public static void setValoracion(double valoracion) {
        ControladoraSearchUsers.valoracion = valoracion;
    }
}
