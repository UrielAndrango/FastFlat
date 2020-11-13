package com.example.fatflat.ui;

public class ControladoraPosts {
    //Vector de fotos
    private static String title= "title";
    private static String description = "description";
    private static String user = "user";
    private static String date = "2020-2-20";
    private static String id = "0";
    private static ControladoraPosts ConstroladoraPosts;
    //Parametros del usuario


    public static String gettitle() {
        return title;
    }

    public static void settitle(String title) {
        ControladoraPosts.title = title;
    }

    public static String getuser() {
        return user;
    }

    public static void setuser(String user) {
        ControladoraPosts.user = user;
    }
    public static String getid() {
        return id;
    }

    public static void setid(String id) {
        ControladoraPosts.id = id;
    }
    public static String getdate() {
        return date;
    }

    public static void setdate(String date) {
        ControladoraPosts.date = date;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescripcion(String descripcion) {
        ControladoraPosts.description = descripcion;
    }

}
