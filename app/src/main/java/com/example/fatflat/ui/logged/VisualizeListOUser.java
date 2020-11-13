package com.example.fatflat.ui.logged;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.ControladoraSearchUsers;
import com.example.fatflat.ui.Offer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class VisualizeListOUser extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_visualize_wish_list:
                    Intent intent = new Intent(VisualizeListOUser.this, VisualizeListWUser.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //onNewIntent(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();

                    //Si lo hacemos con ventanas independientes, quitamos los TRUES
                    return true;
                case R.id.navigation_visualize_offer_list:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_list_o_user);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final TextView NomUsuari = findViewById(R.id.VLO_nomUsuari);
        final ImageView Atras = findViewById(R.id.VLO_User_Atras);
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout_LOU);
        final LinearLayout trophies = findViewById(R.id.layout_trophies_LO_User);
        //USERNAME DEL USUARIO
        NomUsuari.setText(ControladoraSearchUsers.getUsername());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeListOUser.this, MenuPrincipal.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        trophies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizeListOUser.this, ListTrophies.class);
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

        //Valoracion Usuario
        InicialitzarValoracioUsuari();

        //Barra Navegacio Llistes
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_visualize_offer_list);
        //navView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /* Creación de la LISTA DE Offers */
        RequestGetOffers();
    }

    private void InicialitzarValoracioUsuari() {
        //Valoracion Usuario
        final TextView valoracion_usuario = findViewById(R.id.textView_valoracio_numero_VLO_User);
        valoracion_usuario.setText(Double.toString(round(ControladoraSearchUsers.getValoracion(), 2)));
        if (ControladoraSearchUsers.getValoracion() != -1) {
            final ImageView star1 = findViewById(R.id.imageViewStar1_VLO_User);
            final ImageView star2 = findViewById(R.id.imageViewStar2_VLO_User);
            final ImageView star3 = findViewById(R.id.imageViewStar3_VLO_User);
            final ImageView star4 = findViewById(R.id.imageViewStar4_VLO_User);
            final ImageView star5 = findViewById(R.id.imageViewStar5_VLO_User);
            ImageView[] stars = {star1, star2, star3, star4, star5};
            int valoracion = (int) ControladoraSearchUsers.getValoracion();
            for (int i=0; i<valoracion; ++i) {
                stars[i].setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
        }
        else { //no ha realitzat cap intercanvi
            valoracion_usuario.setText("-");
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void RequestGetOffers() {
        final String username = ControladoraSearchUsers.getUsername();
        final ArrayList<Offer> offer_list = new ArrayList<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(VisualizeListOUser.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-offers?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_offer = response.getJSONObject(i);

                        String id = info_offer.getString("id"); //TODO: ACTUALITZA AMB CAMP ID
                        String name = info_offer.getString("name");
                        String category = info_offer.getString("category");
                        String type = info_offer.getString("type");
                        JSONArray keywords_array = info_offer.getJSONArray("keywords");
                        String keywords = "";
                        for(int j = 0; j < keywords_array.length(); ++j) {
                            String keyword = keywords_array.getString(j);
                            keywords += "#";
                            keywords += keyword;
                        }
                        String value = info_offer.getString("value");
                        String description = info_offer.getString("description");
                        Offer offer = new Offer(id,name,Integer.parseInt(category),type,keywords,Integer.parseInt(value),description,ControladoraPresentacio.getUsername());
                        String tipus = offer.getType();
                        System.out.println("El tipus es : "+ tipus);
                        offer_list.add(offer);
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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonArrayRequest);
    }

    private void InicialitzaBotonsOffers(ArrayList<Offer> offer_list) {
        ControladoraPresentacio.setOffer_List(offer_list);
        int numBotones = offer_list.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.listaOffers_LO);

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        LinearLayout pareja = new LinearLayout(VisualizeListOUser.this);
        //Creamos los botones en bucle
        //Antes debemos saber si hay un numero par o impar
        boolean mostrar_producto = true;
        boolean modo_impar = false;
        if (numBotones % 2 == 1) modo_impar = true;

        for (int i=0; i<numBotones; ++i){
            //Modo Layout con pareja, layout de layout con foto+precio+nombre
            //LinearLayout pareja;
            if (mostrar_producto && i%2==0) {
                pareja = new LinearLayout(VisualizeListOUser.this);
                pareja.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams paramsP = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                if (i==0) paramsP.setMargins(0, 20, 0, 20);
                else paramsP.setMargins(0, 0, 0, 20);
                pareja.setLayoutParams(paramsP);
                //pareja.setBackgroundResource(R.drawable.logout_rounded);
            }
            //Definimos el layout y lo que contiene (foto+precio+nombre)
            LinearLayout ll = new LinearLayout(VisualizeListOUser.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            final ImageView foto = new ImageView(VisualizeListOUser.this);
            TextView preu_producte = new TextView(VisualizeListOUser.this);
            TextView nom_producte = new TextView(VisualizeListOUser.this);
            //Asignamos Texto a los textViews
            preu_producte.setText(offer_list.get(i).getValue() + "€");
            nom_producte.setText(offer_list.get(i).getName() + "");
            //Le damos el estilo que queremos
            //pareja.setBackgroundResource(R.drawable.button_rounded);
            ll.setBackgroundResource(R.drawable.button_rounded);
            //foto.setImageURI();
            StorageReference Reference = storageRef.child("/products/" + offer_list.get(i).getId()).child("product_0");
            Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    foto.setImageBitmap(bmp);
                    foto.setVisibility(View.VISIBLE);
                }
            });
            preu_producte.setTextColor(VisualizeListOUser.this.getResources().getColor(R.color.colorLetraKatundu));
            nom_producte.setTextColor(VisualizeListOUser.this.getResources().getColor(R.color.colorLetraKatundu));
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
            if (i%2==0) paramsll.setMargins(0, 0, 10, 0);
            else paramsll.setMargins(10, 0, 0, 0);
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
            ll.setContentDescription(offer_list.get(i).getName());
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new VisualizeListOUser.LayoutOnClickListener(VisualizeListOUser.this));
            //Añadimos el layout dinamico al layout
            ll.addView(foto);
            ll.addView(preu_producte);
            ll.addView(nom_producte);
            if (!mostrar_producto) ll.setVisibility(View.INVISIBLE);
            pareja.addView(ll);
            if (mostrar_producto && i%2 == 0) llBotonera.addView(pareja);

            if (modo_impar == true && i==numBotones-1) {
                --i;
                mostrar_producto = false;
                modo_impar = false;
            }
        }
    }
/*
    private class ButtonsOnClickListener implements View.OnClickListener {
        public ButtonsOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            //Provando que funciona el boton
            Toast.makeText(getApplicationContext(),b.getText(),Toast.LENGTH_SHORT).show();

            Offer info_offer = ControladoraPresentacio.getOffer_perName(b.getHint().toString());
            //Pasamos los datos del deseo a la controladora
            ControladoraPresentacio.setOffer_id(info_offer.getId());
            ControladoraPresentacio.setOffer_name(info_offer.getName());
            ControladoraPresentacio.setOffer_Categoria(info_offer.getCategory());
            boolean type = true;
            String tipus = info_offer.getType();
            System.out.println("El tipus es : "+ tipus);
            if(tipus.equals("Producte")) type = false;
            ControladoraPresentacio.setOffer_Service(type);
            ControladoraPresentacio.setOffer_PC(info_offer.getKeywords());
            ControladoraPresentacio.setOffer_Value(info_offer.getValue());
            ControladoraPresentacio.setOffer_Description(info_offer.getDescription());
            //Nos vamos a la ventana de User
            Intent intent = new Intent(VisualizeListOUser.this, VisualizeOffer.class); //TODO: portar a una nova activity per visualitzar la oferta
            startActivity(intent);
            //finish();
        }
    }
*/
    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(VisualizeListOUser visualizeListOUser) {
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
            boolean type = true;
            String tipus = info_offer.getType();
            if(tipus.equals("Producte")) type = false;
            ControladoraPresentacio.setOffer_Service(type);
            ControladoraPresentacio.setOffer_PC(info_offer.getKeywords());
            ControladoraPresentacio.setOffer_Value(info_offer.getValue());
            ControladoraPresentacio.setOffer_Description(info_offer.getDescription());
            //Nos vamos a la ventana de EditOffer
            Intent intent = new Intent(VisualizeListOUser.this, VisualizeOffer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
        }
    }
}
