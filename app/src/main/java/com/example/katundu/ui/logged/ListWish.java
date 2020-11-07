package com.example.katundu.ui.logged;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.Wish;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListWish extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_wish_list:
                    return true;
                case R.id.navigation_own_list:
                    Intent intent = new Intent(ListWish.this, ListOffer.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //onNewIntent(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    //startActivityForResult(intent, 0);
                    overridePendingTransition(0,0);
                    finish();

                    //Si lo hacemos con ventanas independientes, quitamos los TRUES
                    return true;
                case R.id.navigation_fav_list:
                    Intent intent_fav = new Intent(ListWish.this, ListFavorites.class);
                    intent_fav.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent_fav);
                    overridePendingTransition(0,0);
                    finish();
                    return true;
            }
            return false;
        }
    };

    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wish);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final TextView NomUsuari = findViewById(R.id.nomUsuari);
        final ImageView ImgSettings = findViewById(R.id.img_settings);
        final ImageView Atras = findViewById(R.id.User_Atras);
        final FloatingActionButton addWish = findViewById(R.id.FAB_addWish_LW);
        final LinearLayout trophies = findViewById(R.id.layout_trophies_LW);
        refreshLayout = findViewById(R.id.refreshLayout_LW);

        //USERNAME DEL USUARIO
        NomUsuari.setText(ControladoraPresentacio.getUsername());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListWish.this, MenuPrincipal.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        ImgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListWish.this, Ajustes.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        trophies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListWish.this, ListTrophies.class);
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

        //Esto se descomentara si sabemos volver a atras de forma "inteligente"
        //Si no gusta se comenta y listo
        NomUsuari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListWish.this, EditarPerfil.class);
                startActivity(intent);
                //finish();
            }
        });

        addWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ControladoraAddProduct.reset(); TODO: ESTO PARA QUE ES?
                Intent intent = new Intent(ListWish.this, AddWish.class);
                startActivity(intent);
                //finish();
            }
        });

        InicialitzarValoracioUsuari();

        //Barra Navegacio Llistes
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView.setSelectedItemId(R.id.navigation_own_list);
        //navView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        /* Creación de la LISTA DE WISHES */
        RequestGetWishes();
    }

    private void RequestGetWishes() {
        final ArrayList<Wish> wish_list = new ArrayList<>();
        final String username = ControladoraPresentacio.getUsername();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(ListWish.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-wishes?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_wish = response.getJSONObject(i);

                        String id = info_wish.getString("id");
                        String name = info_wish.getString("name");
                        String category = info_wish.getString("category");
                        String type = info_wish.getString("type");
                        JSONArray keywords_array = info_wish.getJSONArray("keywords");
                        String keywords = "";
                            for(int j = 0; j < keywords_array.length(); ++j) {
                                String keyword = keywords_array.getString(j);
                                keywords += "#";
                                keywords += keyword;
                            }
                        String value = info_wish.getString("value");

                        Wish wish = new Wish(id,name,Integer.parseInt(category),type,keywords,Integer.parseInt(value));

                        wish_list.add(wish);
                    }

                    InicialitzaBotonsWishes(wish_list);

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

    private void InicialitzaBotonsWishes(ArrayList<Wish> wish_list) {
        ControladoraPresentacio.setWish_List(wish_list);

        int numBotones = wish_list.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.listaWishes_LW);

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

        //Creamos los botones en bucle
        for (int i=0; i<numBotones; i++){
            Button button = new Button(this);

            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);

            //Asignamos Texto al botón
            button.setText(wish_list.get(i).getName());

            //Le damos el estilo que queremos
            button.setBackgroundResource(R.drawable.button_rounded);
            button.setTextColor(this.getResources().getColor(R.color.colorLetraKatundu));

            //Margenes del button
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //params.setMargins(left, top, right, bottom);
            if(i == 0) params.setMargins(0, 20, 0, 20);
            else params.setMargins(0, 0, 0, 20);
            button.setLayoutParams(params);

            //Asignamose el Listener
            button.setOnClickListener(new ButtonsOnClickListener());

            //Añadimos el botón a la botonera
            llBotonera.addView(button);
        }
    }

    private void InicialitzarValoracioUsuari() {
        //Valoracion Usuario
        final TextView valoracion_usuario = findViewById(R.id.textView_valoracio_numero_User);
        valoracion_usuario.setText(Double.toString(ControladoraPresentacio.getValoracion()));
        final ImageView star1 = findViewById(R.id.imageViewStar1_User);
        final ImageView star2 = findViewById(R.id.imageViewStar2_User);
        final ImageView star3 = findViewById(R.id.imageViewStar3_User);
        final ImageView star4 = findViewById(R.id.imageViewStar4_User);
        final ImageView star5 = findViewById(R.id.imageViewStar5_User);
        ImageView[] stars = {star1, star2, star3, star4, star5};
        int valoracion = (int)ControladoraPresentacio.getValoracion();
        for (int i=0; i<valoracion; ++i) {
            stars[i].setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }
    }

    private class ButtonsOnClickListener implements View.OnClickListener {
        public ButtonsOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            //Provando que funciona el boton
            //Toast.makeText(getApplicationContext(),b.getText(),Toast.LENGTH_SHORT).show();

            Wish info_wish = ControladoraPresentacio.getWish_perName(b.getText().toString());

            //Pasamos los datos del deseo a la controladora
            ControladoraPresentacio.setWish_id(info_wish.getId());
            ControladoraPresentacio.setWish_name(info_wish.getName());
            ControladoraPresentacio.setWish_Categoria(info_wish.getCategory());

            boolean type = true;
            if(info_wish.getType().equals("Producte")) type = false;

            ControladoraPresentacio.setWish_Service(type);
            ControladoraPresentacio.setWish_PC(info_wish.getKeywords());
            ControladoraPresentacio.setWish_Value(info_wish.getValue());


            //Nos vamos a la ventana de User
            Intent intent = new Intent(ListWish.this, EditWish.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();

        }
    }
}
