package com.example.fatflat.ui.logged;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.Image;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.ControladoraTrophies;
import com.example.fatflat.ui.Offer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class MenuPrincipal extends AppCompatActivity {
    private TextView mTextMessage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    final ArrayList<Offer> ofertes_totals = new ArrayList<Offer>();
    final ArrayList<String> ofertes_matches = new ArrayList<String>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_comprador:
                    return true;
                case R.id.navigation_chats:
                    Intent intent2 = new Intent(MenuPrincipal.this, ListChat.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    //finish();
                    break;
                case R.id.navigation_vendedor:
                    Intent intent3 = new Intent(MenuPrincipal.this, Vendedor.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent3);
                    overridePendingTransition(0,0);
                    //finish();
                    break;
            }
            return false;
        }
    };

    //Notificaciones
    private NotificationManagerCompat notificationManager;


    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Perfil_img = findViewById(R.id.img_perfil);
        final LinearLayout search = findViewById(R.id.search_MP);
        //final ImageView Chats = findViewById(R.id.Chats);

        refreshLayout = findViewById(R.id.refreshLayout_MP);


        //Notificaciones
        notificationManager = NotificationManagerCompat.from(this);
        //Localizacion
        IntentFilter intentFilter = new IntentFilter(LocationManager.MODE_CHANGED_ACTION);
        registerReceiver(locationStateReceiver, intentFilter);

        //Barra navegacion
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //RequestGetMatches();

        Perfil_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Para todas las listas y en cualquier momento, hasta que se diga lo contrario
                ControladoraTrophies.setUsername(ControladoraPresentacio.getUsername());
                //Nos vamos a ListOffer
                Intent intent = new Intent(MenuPrincipal.this, Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuPrincipal.this, SearchProduct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //recreate();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                refreshLayout.setRefreshing(false);
            }
        });
        RequestGetMatches();
    }

    private void RequestGetMatches() {
        final String username = ControladoraPresentacio.getUsername();
        final ArrayList<Offer> offer_list_user = new ArrayList<>();
        final ArrayList<Offer> offer_list_match = new ArrayList<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(MenuPrincipal.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-matches?un=" + ControladoraPresentacio.getUsername();

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject arrays_offer = response.getJSONObject(i);
                        for(int j = 0; j < arrays_offer.length();++j)
                        {
                            JSONObject info_offer = arrays_offer.getJSONObject("offer"+ Integer.toString(j+1));
                            String user =info_offer.getString("user");
                            String id = info_offer.getString("id"); //TODO: ACTUALITZA AMB CAMP ID
                            String name = info_offer.getString("name");
                            String category = info_offer.getString("category");
                            String type = info_offer.getString("type");
                            JSONArray keywords_array = info_offer.getJSONArray("keywords");
                            String keywords = "";
                            for(int k = 0; k < keywords_array.length(); ++k) {
                                String keyword = keywords_array.getString(k);
                                keywords += "#";
                                keywords += keyword;
                            }
                            String value = info_offer.getString("value");
                            String description = info_offer.getString("description");
                            Offer offer = new Offer(id,name,Integer.parseInt(category),type,keywords,Integer.parseInt(value),description,user);
                            if(j==0)
                            {
                                offer_list_user.add(offer);
                                ofertes_matches.add(offer.getId());
                            }
                            else
                            {
                                offer_list_match.add(offer);
                                ofertes_matches.add(offer.getId());
                                ofertes_totals.add(offer);
                            }
                        }
                    }
                    InicialitzaBotonsOffersv2(offer_list_user,offer_list_match);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void RequestGetOffers() {
        final String username = ControladoraPresentacio.getUsername();
        final ArrayList<Offer> offer_list = new ArrayList<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(MenuPrincipal.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-alloffers";

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_offer = response.getJSONObject(i);
                        String user = info_offer.getString("user");
                        if(!user.equals(ControladoraPresentacio.getUsername())) {
                            String id = info_offer.getString("id"); //TODO: ACTUALITZA AMB CAMP ID
                            String name = info_offer.getString("name");
                            String category = info_offer.getString("category");
                            String type = info_offer.getString("type");
                            JSONArray keywords_array = info_offer.getJSONArray("keywords");
                            String keywords = "";
                            for (int j = 0; j < keywords_array.length(); ++j) {
                                String keyword = keywords_array.getString(j);
                                keywords += "#";
                                keywords += keyword;
                            }
                            String value = info_offer.getString("value");
                            String description = info_offer.getString("description");
                            Offer offer = new Offer(id, name, Integer.parseInt(category), type, keywords, Integer.parseInt(value), description,user);
                            offer_list.add(offer);
                        }

                    }
                    InicialitzaBotonsOffers(offer_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void InicialitzaBotonsOffers(ArrayList<Offer> offer_list) {
        for (int i = 0; i < offer_list.size();++i)
        {
            ofertes_totals.add(offer_list.get(i));
            System.out.println(offer_list.get(i).getId());
            if (ofertes_matches.contains(offer_list.get(i).getId()))
            {
                offer_list.remove(offer_list.get(i));
            }
        }
        ControladoraPresentacio.setOffer_List(ofertes_totals);
        int numBotones = offer_list.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.listaOffers_gen);

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        LinearLayout pareja = new LinearLayout(MenuPrincipal.this);
        //Creamos los botones en bucle
        //Antes debemos saber si hay un numero par o impar
        boolean mostrar_producto = true;
        boolean modo_impar = false;
        if (numBotones % 2 == 1) modo_impar = true;

        for (int i=0; i<numBotones; ++i){
            //Modo Layout con pareja, layout de layout con foto+precio+nombre
            //LinearLayout pareja;
            if (mostrar_producto && i%2==0) {
                pareja = new LinearLayout(MenuPrincipal.this);
                pareja.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams paramsP = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                if (i==0) paramsP.setMargins(0, 20, 0, 20);
                else paramsP.setMargins(0, 0, 0, 20);
                pareja.setLayoutParams(paramsP);
                //pareja.setBackgroundResource(R.drawable.logout_rounded);
            }
            //CardView para esquinas redondeadas
            CardView cv = new CardView(MenuPrincipal.this);
            //Definimos el layout y lo que contiene (foto+precio+nombre)
            LinearLayout ll = new LinearLayout(MenuPrincipal.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout preu_eye = new LinearLayout(MenuPrincipal.this);
            preu_eye.setOrientation(LinearLayout.HORIZONTAL);
            final ImageView foto = new ImageView(MenuPrincipal.this);
            TextView preu_producte = new TextView(MenuPrincipal.this);
            final ImageView icon_eye = new ImageView(MenuPrincipal.this);
            TextView nom_producte = new TextView(MenuPrincipal.this);
            //Asignamos Texto a los textViews
            preu_producte.setText(offer_list.get(i).getValue() + "€");
            nom_producte.setText(offer_list.get(i).getName() + "");
            //Le damos el estilo que queremos
            //pareja.setBackgroundResource(R.drawable.button_rounded);
            cv.setRadius(16);
            //ll.setBackgroundResource(R.drawable.layout_rounded);
            ll.setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));
            //foto.setImageURI();
            StorageReference Reference = storageRef.child("/products/" + offer_list.get(i).getId()).child("product_0");
            Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    //bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    foto.setImageBitmap(bmp);
                    foto.setVisibility(View.VISIBLE);
                }
            });
            preu_producte.setTextColor(MenuPrincipal.this.getResources().getColor(R.color.colorLetraKatundu));
            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye);
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            //Redondeamos las esquinas de las fotos
            //bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2*8);
            icon_eye.setImageBitmap(bmp);
            //if (i%2 == 0) icon_eye.setVisibility(View.INVISIBLE);
            icon_eye.setVisibility(View.VISIBLE);
            if (i != 0) icon_eye.setVisibility(View.INVISIBLE);

            icon_eye.setColorFilter(getResources().getColor(R.color.colorLetraKatundu));
            preu_producte.setTextColor(getResources().getColor(R.color.colorLetraKatundu));
            nom_producte.setTextColor(getResources().getColor(R.color.colorLetraKatundu));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            preu_producte.setTypeface(boldTypeface);
            nom_producte.setTypeface(boldTypeface);
            preu_producte.setTextSize(18);
            nom_producte.setTextSize(16);
            //Margenes del layout
            TableRow.LayoutParams paramscv = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramscv.weight = 1;
            //paramscv.height = 500;
            //paramsll.setMargins(left, top, right, bottom);
            if (i%2==0) paramscv.setMargins(0, 0, 10, 0);
            else paramscv.setMargins(10, 0, 0, 0);
            cv.setLayoutParams(paramscv);
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsll.weight = 1;
            paramsll.height = 700;
            //paramsll.setMargins(left, top, right, bottom);
            if (i%2==0) paramsll.setMargins(0, 0, 0, 0);
            else paramsll.setMargins(0, 0, 0, 0);
            ll.setLayoutParams(paramsll);
            TableRow.LayoutParams paramsPreuEye = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPreuEye.setMargins(0, 0, 0, 0);
            preu_eye.setLayoutParams(paramsPreuEye);
            //Margenes de los textViews
            TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsFoto.weight = 1;
            //paramsFoto.height = 600;
            paramsFoto.setMargins(0, 0, 0, 10);
            foto.setLayoutParams(paramsFoto);
            foto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPrecio.weight = 1;
            paramsPrecio.setMargins(25, 10, 25, 10);
            preu_producte.setLayoutParams(paramsPrecio);
            TableRow.LayoutParams paramsIconEye = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsIconEye.width = 70;
            paramsIconEye.height = 70;
            paramsIconEye.setMargins(25, 10, 25, 10);
            icon_eye.setLayoutParams(paramsIconEye);
            TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsN.weight = 1;
            paramsN.setMargins(25, 10, 25, 20);
            nom_producte.setLayoutParams(paramsN);
            //Le escondemos el nombre del producto en la descripcion
            ll.setContentDescription(offer_list.get(i).getName());
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new MenuPrincipal.LayoutOnClickListener(MenuPrincipal.this));
            //Añadimos el layout dinamico al layout
            preu_eye.addView(preu_producte);
            preu_eye.addView(icon_eye);
            ll.addView(foto);
            ll.addView(preu_eye);
            ll.addView(nom_producte);
            cv.addView(ll);
            if (!mostrar_producto) cv.setVisibility(View.INVISIBLE);
            pareja.addView(cv);
            if (mostrar_producto && i%2 == 0) llBotonera.addView(pareja);

            if (modo_impar == true && i==numBotones-1) {
                --i;
                mostrar_producto = false;
                modo_impar = false;
            }
        }
    }

    private void InicialitzaBotonsOffersv2(ArrayList<Offer> offer_user,ArrayList<Offer> offer_match) {
        //ControladoraPresentacio.setOffer_List(offer_list);
        int numBotones = offer_user.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.listaOffers_gen);
        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        LinearLayout pareja = new LinearLayout(MenuPrincipal.this);
        //Creamos los botones en bucle
        //Antes debemos saber si hay un numero par o impar
        boolean mostrar_producto = true;
        boolean modo_impar = false;
        if (numBotones % 2 == 1) modo_impar = true;

        for (int i=0; i<numBotones; ++i){
            //Modo Layout con pareja, layout de layout con foto+precio+nombre
            //LinearLayout pareja;
            if (mostrar_producto) {
                pareja = new LinearLayout(MenuPrincipal.this);
                pareja.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams paramsP = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                if (i==0) paramsP.setMargins(0, 20, 0, 20);
                else paramsP.setMargins(0, 0, 0, 20);
                pareja.setLayoutParams(paramsP);
                pareja.setBackgroundResource(R.drawable.customborder);
            }
            for(int j = 0; j<2;++j) {
                //Definimos el layout y lo que contiene (foto+precio+nombre)
                if (j == 0) {
                    LinearLayout ll = new LinearLayout(MenuPrincipal.this);

                    ll.setOrientation(LinearLayout.VERTICAL);
                    final ImageView foto = new ImageView(MenuPrincipal.this);
                    TextView preu_producte = new TextView(MenuPrincipal.this);
                    TextView nom_producte = new TextView(MenuPrincipal.this);
                    //Asignamos Texto a los textViews
                    preu_producte.setText(offer_user.get(i).getValue() + "€");
                    nom_producte.setText(offer_user.get(i).getName() + "");
                    //Le damos el estilo que queremos
                    //pareja.setBackgroundResource(R.drawable.button_rounded);
                    ll.setBackgroundResource(R.drawable.button_rounded);
                    //ll.setBackgroundColor(Color.WHITE);
                    //ll.setBackgroundResource(R.drawable.custom_border_black);
                    //foto.setImageURI();
                    StorageReference Reference = storageRef.child("/products/" + offer_user.get(i).getId()).child("product_0");
                    Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            //Redondeamos las esquinas de las fotos
                            bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp, 64 * 2);
                            foto.setImageBitmap(bmp);
                            foto.setVisibility(View.VISIBLE);
                        }
                    });
                    preu_producte.setTextColor(MenuPrincipal.this.getResources().getColor(R.color.colorLetraKatundu));
                    nom_producte.setTextColor(MenuPrincipal.this.getResources().getColor(R.color.colorLetraKatundu));
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    preu_producte.setTypeface(boldTypeface);
                    nom_producte.setTypeface(boldTypeface);
                    preu_producte.setTextSize(18);
                    nom_producte.setTextSize(18);
                    //Margenes del layout
                    TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsll.weight = 1;
                    paramsll.height = 800;
                    //paramsll.setMargins(left, top, right, bottom);
                    paramsll.setMargins(0, 0, 10, 0);
                    ll.setLayoutParams(paramsll);
                    //Margenes de los textViews
                    TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsFoto.weight = 1;
                    paramsFoto.setMargins(25, 25, 25, 10);
                    foto.setLayoutParams(paramsFoto);
                    TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsPrecio.setMargins(25, 10, 25, 10);
                    preu_producte.setLayoutParams(paramsPrecio);
                    TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    //paramsN.weight = 1;
                    paramsN.setMargins(25, 10, 25, 20);
                    nom_producte.setLayoutParams(paramsN);
                    //Le escondemos el nombre del producto en la descripcion
                    ll.setContentDescription(offer_user.get(i).getName());
                    //Asignamose el Listener al Layout dinamico
                    ll.setOnClickListener(new MenuPrincipal.LayoutOnClickListener(MenuPrincipal.this));
                    //Añadimos el layout dinamico al layout
                    ll.addView(foto);
                    ll.addView(preu_producte);
                    ll.addView(nom_producte);
                    if (!mostrar_producto) ll.setVisibility(View.INVISIBLE);
                    pareja.addView(ll);
                    if (mostrar_producto && j == 0) llBotonera.addView(pareja);


                }
                else
                {
                    LinearLayout ll = new LinearLayout(MenuPrincipal.this);

                    ll.setOrientation(LinearLayout.VERTICAL);
                    final ImageView foto = new ImageView(MenuPrincipal.this);
                    TextView preu_producte = new TextView(MenuPrincipal.this);
                    TextView nom_producte = new TextView(MenuPrincipal.this);
                    //Asignamos Texto a los textViews
                    preu_producte.setText(offer_match.get(i).getValue() + "€");
                    nom_producte.setText(offer_match.get(i).getName() + "");
                    //Le damos el estilo que queremos
                    //pareja.setBackgroundResource(R.drawable.button_rounded);
                    ll.setBackgroundResource(R.drawable.button_rounded);
                    //ll.setBackgroundColor(Color.WHITE);
                    //ll.setBackgroundResource(R.drawable.custom_border_black);
                    //foto.setImageURI();
                    StorageReference Reference = storageRef.child("/products/" + offer_match.get(i).getId()).child("product_0");
                    Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            //Redondeamos las esquinas de las fotos
                            bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp, 64 * 2);
                            foto.setImageBitmap(bmp);
                            foto.setVisibility(View.VISIBLE);
                        }
                    });
                    preu_producte.setTextColor(MenuPrincipal.this.getResources().getColor(R.color.colorLetraKatundu));
                    nom_producte.setTextColor(MenuPrincipal.this.getResources().getColor(R.color.colorLetraKatundu));
                    Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                    preu_producte.setTypeface(boldTypeface);
                    nom_producte.setTypeface(boldTypeface);
                    preu_producte.setTextSize(18);
                    nom_producte.setTextSize(18);
                    //Margenes del layout
                    TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsll.weight = 1;
                    paramsll.height = 800;
                    //paramsll.setMargins(left, top, right, bottom);
                    paramsll.setMargins(10, 0, 0, 0);
                    ll.setLayoutParams(paramsll);
                    //Margenes de los textViews
                    TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsFoto.weight = 1;
                    paramsFoto.setMargins(25, 25, 25, 10);
                    foto.setLayoutParams(paramsFoto);
                    TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    paramsPrecio.setMargins(25, 10, 25, 10);
                    preu_producte.setLayoutParams(paramsPrecio);
                    TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    //paramsN.weight = 1;
                    paramsN.setMargins(25, 10, 25, 20);
                    nom_producte.setLayoutParams(paramsN);
                    //Le escondemos el nombre del producto en la descripcion
                    ll.setContentDescription(offer_match.get(i).getName());
                    //Asignamose el Listener al Layout dinamico
                    ll.setOnClickListener(new MenuPrincipal.LayoutOnClickListener(MenuPrincipal.this));
                    //Añadimos el layout dinamico al layout
                    ll.addView(foto);
                    ll.addView(preu_producte);
                    ll.addView(nom_producte);
                    if (!mostrar_producto) ll.setVisibility(View.INVISIBLE);
                    pareja.addView(ll);
                    if (mostrar_producto && j == 0) llBotonera.addView(pareja);


                }

            }
        }
        RequestGetOffers();
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(MenuPrincipal MenuPrincipal) {
        }
        @Override
        public void onClick(View view) {
            //Provando que funciona el layout en modo boton
            Toast.makeText(getApplicationContext(),view.getContentDescription().toString(),Toast.LENGTH_SHORT).show();

            //Pasamos los datos del deseo a la controladora
            Offer info_offer = ControladoraPresentacio.getOffer_perName(view.getContentDescription().toString());
            //Pasamos los datos del deseo a la controladora
            ControladoraPresentacio.setOffer_id(info_offer.getId());
            ControladoraPresentacio.setOffer_name(info_offer.getName());
            ControladoraPresentacio.setOffer_Categoria(info_offer.getCategory());
            ControladoraPresentacio.setOffer_user(info_offer.getUser());
            boolean type = true;
            String tipus = info_offer.getType();
            if(tipus.equals("Producte")) type = false;
            ControladoraPresentacio.setOffer_Service(type);
            ControladoraPresentacio.setOffer_PC(info_offer.getKeywords());
            ControladoraPresentacio.setOffer_Value(info_offer.getValue());
            ControladoraPresentacio.setOffer_Description(info_offer.getDescription());
            //Nos vamos a la ventana de EditOffer
            Intent intent = new Intent(MenuPrincipal.this, VisualizeOffer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
        }
    }

    private BroadcastReceiver locationStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = getString(R.string.Notification_title);
            String body = getString(R.string.Notification_body);
            if (checkIfLocationOpened()){
                notifica(title, body);
            }
        }
    };

    private boolean checkIfLocationOpened() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        //System.out.println("Provider contains=> " + provider);
        if (provider.contains("gps") || provider.contains("network")){
            return true;
        }
        return false;
    }

    public void notifica(String title, String text) {
        String body = getString(R.string.Notification_body);
        Intent intent = new Intent(this, MenuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Drawable drawable = getResources().getDrawable(R.drawable.logo_fatflat);
        Bitmap myBitmap = ((BitmapDrawable) drawable).getBitmap();

        Notification not = new NotificationCompat.Builder(this, ConfigureNotifications.CHANNEL_1)
                .setSmallIcon(R.drawable.icon_building)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setLargeIcon(myBitmap)
                //.setStyle(new NotificationCompat.BigPictureStyle()
                //        .bigPicture(myBitmap)
                //        .bigLargeIcon(null))
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1,not);
    }
}
