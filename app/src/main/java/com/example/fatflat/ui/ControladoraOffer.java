package com.example.fatflat.ui;

public class ControladoraOffer {
    private static boolean tracking = true;
    private static int accuracy = 96;
    private static boolean venta = true;
    private static String tipo_inmueble = "piso";
    private static String poblacion = "Cornellá de Llobregat";
    private static String zona = "Sant Ildefons";
    private static int precio = 195000;
    private static int superficie = 67;
    private static int numero_habitaciones = 3;
    private static int numero_banyos = 1;
    private static String fecha_publicacion = "09/12/2020";
    private static String estado_inmueble = "Casi nuevo";
    private static boolean[] zonas_adicionales = {false, true, false, false, false, false};
    private static boolean[] extras = {true, false, true, true, true};

    public static boolean isTracking() {
        return tracking;
    }

    public static int getAccuracy() {
        return accuracy;
    }

    public static boolean isVenta() {
        return venta;
    }

    public static String getTipo_inmueble() {
        return tipo_inmueble;
    }

    public static String getPoblacion() {
        return poblacion;
    }

    public static String getZona() {
        return zona;
    }

    public static int getPrecio() {
        return precio;
    }

    public static int getSuperficie() {
        return superficie;
    }

    public static int getNumero_habitaciones() {
        return numero_habitaciones;
    }

    public static int getNumero_banyos() {
        return numero_banyos;
    }

    public static String getFecha_publicacion() {
        return fecha_publicacion;
    }

    public static String getEstado_inmueble() {
        return estado_inmueble;
    }

    public static boolean[] getZonas_adicionales() {
        return zonas_adicionales;
    }

    public static boolean[] getExtras() {
        return extras;
    }
}
