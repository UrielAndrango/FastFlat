package com.example.fatflat.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraChat;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.Offer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EndExchange extends AppCompatActivity {

    String id_offer1;
    String id_offer2;
    int usuari;
    boolean reintento = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_exchange);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.EndE_Atras);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndExchange.this, ListChat.class);
                onNewIntent(intent);
                finish();
            }
        });

        usuari = 1;
        RequestGetOffers();
    }

    private void RequestEndExchange() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EndExchange.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/end-exchange?" +
                "id1=" + id_offer1 + "&" +
                "id2=" + id_offer2;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //message added successfully
                            Intent intent = new Intent(getApplicationContext(), RateExchange.class);
                            startActivity(intent);
                            finish();
                        }
                        else { //response == "1" No such user in the database
                            String texterror = getString(R.string.add_post_general_error);
                            Toast toast = Toast.makeText(EndExchange.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            usuari = 1;
                            reintento = true;
                            RequestGetOffers();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.add_post_general_error);
                Toast toast = Toast.makeText(EndExchange.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                usuari = 1;
                reintento = true;
                RequestGetOffers();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RequestGetOffers() {
        final String username;
        if(usuari == 1)
            username = ControladoraPresentacio.getUsername();
        else username = ControladoraChat.getUsername2();

        final ArrayList<Offer> offer_list = new ArrayList<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(EndExchange.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-offers?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_offer = response.getJSONObject(i);
                        String id = info_offer.getString("id");
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
        queue.add(jsonArrayRequest);
    }


    @SuppressLint("SetTextI18n")
    private void InicialitzaBotonsOffers(ArrayList<Offer> offer_list) {
        ControladoraPresentacio.setOffer_List(offer_list);

        int numBotones = offer_list.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.LinearLayout_Ofertes_end_exchange);

        if(reintento) {
            reintento = false;
            llBotonera.removeAllViewsInLayout();
        }

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

        TextView textView = new TextView(this);
        textView.setLayoutParams(lp);
        if(usuari == 1)
            textView.setText(getString(R.string.offers_exchanged1));
        else
            textView.setText(getString(R.string.offers_exchanged2));
        textView.setTextColor(this.getResources().getColor(R.color.colorAccent));
        textView.setTextSize(20);
        TableRow.LayoutParams params2 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        params2.setMargins(0, 20, 0, 20);
        textView.setLayoutParams(params2);
        llBotonera.addView(textView);


        //Creamos los botones en bucle
        for (int i=0; i<numBotones; i++){
            Button button = new Button(this);

            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);

            //Asignamos Texto al botón
            button.setText(offer_list.get(i).getName());

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

    private class ButtonsOnClickListener implements View.OnClickListener {
        public ButtonsOnClickListener() {
        }

        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            //Provando que funciona el boton
            //Toast.makeText(getApplicationContext(),b.getText(),Toast.LENGTH_SHORT).show();

            //Pasamos los datos del deseo a la controladora
            Offer info_offer = ControladoraPresentacio.getOffer_perName(b.getText().toString());

            if(usuari == 1) {
                id_offer1 = info_offer.getId();
                usuari = 2;
                RequestGetOffers();
            }
            else {
                id_offer2 = info_offer.getId();
                RequestEndExchange();
            }
        }
    }
}
