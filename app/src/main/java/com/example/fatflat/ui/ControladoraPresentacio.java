package com.example.fatflat.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import java.util.ArrayList;

public class ControladoraPresentacio {
    //ATRIBUTS USER
    private static String username = "null";
    private static String nom_real = "testname";
    private static String password = "password";
    private static String latitud = "0.0";
    private static String longitud = "0.0";
    private static String latitudMapa = "";
    private static String longitudMapa = "";
    private static String distanciaMaxima = "10.0";
    private static double valoracion = 4;
    private static String descriptionUser = "";
    private static String birthdate = "";
    private static ImageView profile_picture;
    private static String question = "";
    private static String answer = "";
    private static int intentosLogin = 0;
    private static int intentosSecurityLogin = 0;

    //idioma usuario (temporal)
    private static String idioma = "";

    //ATRIBUTS add product
    private static int numero_imagen = -1;
    private static int cantidad_fotos = 0;
    private static int numero_maximo_fotos = 5;
    private static Uri[] fotos = new Uri[numero_maximo_fotos];

    //ATRIBUTS WISH
    private static ArrayList<Wish> wish_list = new ArrayList<>();
    private static String wish_id = "wish_id";
    private static String wish_name = "Audi";
    private static int wish_categoria = 5;
    private static boolean wish_service = false;
    private static String wish_PC = "Coche";
    private static Integer value = 0;

    //ATRIBUTS OFFER
    private static ArrayList<Offer> offer_list = new ArrayList<>();
    private static String offer_id = "wish_id";
    private static String offer_name = "Audi";
    private static Integer offer_categoria = 5;
    private static boolean offer_service = false;
    private static String offer_PC = "Coche";
    private static Integer offer_value = 0;
    private static String offer_description= "Es una descripcio";
    private static String offer_user="User";

    //ATRIBUTS Favorite
    private static ArrayList<Favorite> favorite_list = new ArrayList<>();
    private static String favorite_id = "wish_id";
    private static String favorite_name = "Audi";
    private static Integer favorite_categoria = 5;
    private static boolean favorite_service = false;
    private static String favorite_PC = "Coche";
    private static Integer favorite_value = 0;
    private static String favorite_description= "Es una descripcio";

    //ATRIBUTS Post
    private static ArrayList<Post> post_list = new ArrayList<>();
    private static String title = "wish_id";
    private static String description = "Audi";
    private static String user = "wish_id";
    private static String time = "Audi";


    //GET & SET DE USER
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ControladoraPresentacio.username = username;
    }

    public static String getNom_real() {
        return nom_real;
    }

    public static void setNom_real(String nom_real) {
        ControladoraPresentacio.nom_real = nom_real;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ControladoraPresentacio.password = password;
    }

    public static String getLatitud() {
        return latitud;
    }

    public static void setLatitud(String latitud) {
        ControladoraPresentacio.latitud = latitud;
    }

    public static String getLongitud() {
        return longitud;
    }

    public static void setLongitud(String longitud) {
        ControladoraPresentacio.longitud = longitud;
    }

    public static String getLatitudMapa() {
        return latitudMapa;
    }

    public static void setLatitudMapa(String latitudMapa) {
        ControladoraPresentacio.latitudMapa = latitudMapa;
    }

    public static String getLongitudMapa() {
        return longitudMapa;
    }

    public static void setLongitudMapa(String longitudMapa) {
        ControladoraPresentacio.longitudMapa = longitudMapa;
    }

    public static String getDistanciaMaxima() {
        return distanciaMaxima;
    }

    public static void setDistanciaMaxima(String distanciaMaxima) {
        ControladoraPresentacio.distanciaMaxima = distanciaMaxima;
    }

    public static double getValoracion() {
        return valoracion;
    }

    public static void setValoracion(double valoracion) {
        ControladoraPresentacio.valoracion = valoracion;
    }


    //GET & SET add product
    public static int getNumero_imagen() {
        return numero_imagen;
    }

    public static void setNumero_imagen(int numero_imagen) {
        ControladoraPresentacio.numero_imagen = numero_imagen;
    }

    public static int getCantidad_fotos() {
        return cantidad_fotos;
    }

    public static void setCantidad_fotos(int cantidad_fotos) {
        ControladoraPresentacio.cantidad_fotos = cantidad_fotos;
    }

    public static int getNumero_maximo_fotos() {
        return numero_maximo_fotos;
    }

    public static Uri[] getFotos() {
        return fotos;
    }

    public static void setFotos(Uri[] foto) {
        ControladoraPresentacio.fotos = foto;
    }

    public static Uri obtener_foto(int i) {
        //"i" empieza en 0
        Uri foto = fotos[i];
        return foto;
    }

    public static void add_foto(Uri photo, int pos) {
        //"i" empieza en 0
        fotos[pos] = photo;
        ++cantidad_fotos;
    }

    public static void borrar_foto(int i) {
        //"i" empieza en 0
        fotos[i] = null;
        if (cantidad_fotos > 0) --cantidad_fotos;
    }

    public static void reset_fotos() {
        //"i" empieza en 0
        numero_imagen = 1;
        cantidad_fotos = 0;
        fotos = new Uri[numero_maximo_fotos];
    }

    public static void reordenar_fotos() {
        Uri[] actualizada = new Uri[numero_maximo_fotos];
        int new_pos = 0;
        for (int i=0; i<numero_maximo_fotos; ++i) {
            if (fotos[i] != null) {
                actualizada[new_pos] = fotos[i];
                ++new_pos;
            }
        }
        fotos = actualizada;
    }


    //GET & SET DE OFFER
    public static Offer getOffer_perName(String offer_name) {
        boolean trobat = false;
        Offer info_offer = new Offer();
        for(int i = 0; i < offer_list.size() & !trobat; ++i){
            System.out.println(offer_list.get(i).getName());
            System.out.println(offer_name);
            if(offer_list.get(i).getName().equals(offer_name)){
                trobat = true;
                info_offer = offer_list.get(i);
                System.out.println(trobat);
            }
        }
        return info_offer;
    }

    public static void setOffer_List(ArrayList<Offer> offer_list) {
        ControladoraPresentacio.offer_list = offer_list;
    }

    public static String getOffer_name() {
        return offer_name;
    }

    public static void setOffer_name(String offer_name) {
        ControladoraPresentacio.offer_name = offer_name;
    }
    public static String getOffer_user() {
        return offer_user;
    }

    public static void setOffer_user(String offer_user) {
        ControladoraPresentacio.offer_user = offer_user;
    }

    public static int getOffer_Categoria() {
        return offer_categoria;
    }

    public static void setOffer_Categoria(Integer categoria) {
        ControladoraPresentacio.offer_categoria = categoria;
    }

    public static boolean isOffer_Service() {
        return offer_service;
    }

    public static void setOffer_Service(boolean service) {
        ControladoraPresentacio.offer_service = service;
    }

    public static String getOffer_PC() {
        return offer_PC;
    }

    public static void setOffer_PC(String offer_PC) {
        ControladoraPresentacio.offer_PC = offer_PC;
    }

    public static String getOffer_id() {
        return offer_id;
    }

    public static void setOffer_id(String offer_id) {
        ControladoraPresentacio.offer_id = offer_id;
    }


    public static Integer getOffer_Value() {
        return offer_value;
    }

    public static void setOffer_Value(Integer value) {
        ControladoraPresentacio.offer_value = value;
    }
    public static String getOffer_Description() {
        return offer_description;
    }

    public static void setOffer_Description(String description) {
        ControladoraPresentacio.offer_description= description;
    }



    //GET & SET DE WISH
    public static ArrayList<Wish> getWish_List() {
        return wish_list;
    }

    public static Wish getWish_perName(String wish_name) {
        Wish info_wish = new Wish();
        boolean trobat = false;

        for(int i = 0; i < wish_list.size() & !trobat; ++i){
            if(wish_list.get(i).getName().equals(wish_name)){
                trobat = true;
                info_wish = wish_list.get(i);
            }
        }
        return info_wish;
    }



    public static void setWish_List(ArrayList<Wish> wish_list) {
        ControladoraPresentacio.wish_list = wish_list;
    }

    public static String getWish_name() {
        return wish_name;
    }

    public static void setWish_name(String wish_name) {
        ControladoraPresentacio.wish_name = wish_name;
    }

    public static int getWish_Categoria() {
        return wish_categoria;
    }

    public static void setWish_Categoria(int categoria) {
        ControladoraPresentacio.wish_categoria = categoria;
    }

    public static boolean isWish_Service() {
        return wish_service;
    }

    public static void setWish_Service(boolean service) {
        ControladoraPresentacio.wish_service = service;
    }

    public static String getWish_PC() {
        return wish_PC;
    }

    public static void setWish_PC(String wish_PC) {
        ControladoraPresentacio.wish_PC = wish_PC;
    }

    public static String getIdioma() {
        return idioma;
    }

    public static void setIdioma(String idioma) {
        ControladoraPresentacio.idioma = idioma;
    }

    public static String getWish_id() {
        return wish_id;
    }


    public static void setWish_id(String wish_id) {
        ControladoraPresentacio.wish_id = wish_id;
    }

    public static Integer getWish_Value() {
        return value;
    }

    public static void setWish_Value(Integer value) {
        ControladoraPresentacio.value = value;
    }

    public static void afegir_offer_id(String toString) {
    }


    //GET & SET DE FAVORITES
    public static Favorite getfavorite_perName(String favorite_name) {
        boolean trobat = false;
        Favorite info_favorite = new Favorite();
        for(int i = 0; i < favorite_list.size() & !trobat; ++i){
            if(favorite_list.get(i).getName().equals(favorite_name)){
                trobat = true;
                info_favorite = favorite_list.get(i);
            }
        }
        return info_favorite;
    }

    public static boolean isFavorite_withID(String id) {
        boolean trobat = false;
        for(int i = 0; i < favorite_list.size() & !trobat; ++i){
            if(favorite_list.get(i).getId().equals(id)){
                trobat = true;
            }
        }
        return trobat;
    }

    public static void deleteFavorite_byId(String id) {
        boolean trobat = false;
        for(int i = 0; i < favorite_list.size() & !trobat; ++i){
            if(favorite_list.get(i).getId().equals(id)){
                ControladoraPresentacio.favorite_list.remove(i);
                trobat = true;
            }
        }
    }

    public static void addavorite_byId(String id) {
        Favorite favorite = new Favorite();
        favorite.setId(id);
        ControladoraPresentacio.favorite_list.add(favorite);
    }

    public static void setFavorite_List(ArrayList<Favorite> favorite_list) {
        ControladoraPresentacio.favorite_list =favorite_list;
    }

    public static String getFavorite_name() {
        return favorite_name;
    }

    public static void setFavorite_name(String favorite_name) {
        ControladoraPresentacio.favorite_name = favorite_name;
    }

    public static int getFavorite_Categoria() {
        return favorite_categoria;
    }

    public static void setFavorite_Categoria(Integer categoria) {
        ControladoraPresentacio.favorite_categoria = categoria;
    }

    public static boolean isfavorite_Service() {
        return favorite_service;
    }

    public static void setFavorite_Service(boolean service) {
        ControladoraPresentacio.favorite_service = service;
    }

    public static String getfavorite_PC() {
        return favorite_PC;
    }

    public static void setfavorite_PC(String favorite_PC) {
        ControladoraPresentacio.favorite_PC =favorite_PC;
    }

    public static String getfavorite_id() {
        return favorite_id;
    }

    public static void setfavorite_id(String favorite_id) {
        ControladoraPresentacio.favorite_id = favorite_id;
    }


    public static Integer getfavorite_Value() {
        return favorite_value;
    }

    public static void setfavorite_Value(Integer value) {
        ControladoraPresentacio.favorite_value = value;
    }
    public static String getfavorite_Description() {
        return favorite_description;
    }

    public static void setfavorite_Description(String description) {
        ControladoraPresentacio.favorite_description= description;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    //POSTS
    public static void setPost_List(ArrayList<Post> post_list) {
        ControladoraPresentacio.post_list = post_list;
    }

    public static Post getpost_perName(String post_id) {
        boolean trobat = false;
        Post info_post = new Post();
        for(int i = 0; i < post_list.size() & !trobat; ++i){
            if(post_list.get(i).getId().equals(post_id)){
                trobat = true;
                info_post =post_list.get(i);
            }
        }
        return info_post;
    }

    public static String getDescriptionUser() {
        return descriptionUser;
    }

    public static void setDescriptionUser(String descriptionUser) {
        ControladoraPresentacio.descriptionUser = descriptionUser;
    }

    public static String getBirthdate() {
        return birthdate;
    }

    public static void setBirthdate(String birthdate) {
        ControladoraPresentacio.birthdate = birthdate;
    }

    public static ImageView getProfile_picture() {
        return profile_picture;
    }

    public static void setProfile_picture(ImageView profile_picture) {
        ControladoraPresentacio.profile_picture = profile_picture;
    }

    public static int getIntentosLogin() {
        return intentosLogin;
    }

    public static void setIntentosLogin(int intentosLogin) {
        ControladoraPresentacio.intentosLogin = intentosLogin;
    }

    public static int getIntentosSecurityLogin() {
        return intentosSecurityLogin;
    }

    public static void setIntentosSecurityLogin(int intentosSecurityLogin) {
        ControladoraPresentacio.intentosSecurityLogin = intentosSecurityLogin;
    }

    public static String getQuestion() {
        return question;
    }

    public static void setQuestion(String question) {
        ControladoraPresentacio.question = question;
    }

    public static String getAnswer() {
        return answer;
    }

    public static void setAnswer(String answer) {
        ControladoraPresentacio.answer = answer;
    }
}
