package com.example.fatflat.ui.logged;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraAddProduct;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.Offer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Vendedor extends AppCompatActivity {

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
                    Intent intent = new Intent(Vendedor.this, MenuPrincipal.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    //finish();
                    break;
                case R.id.navigation_vendedor:
                    return true;
            }
            return false;
        }
    };
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final FloatingActionButton addProperty = findViewById(R.id.FAB_addProperty_Vendedor);

        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControladoraAddProduct.reset();
                Intent intent = new Intent(Vendedor.this, AddProperty.class);
                startActivity(intent);
                //finish();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout_SP);

        BottomNavigationView navView = (BottomNavigationView) findViewById(R.id.nav_view_sorprenme);
        navView.setSelectedItemId(R.id.navigation_vendedor);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

        RequestGetOffers();
    }

    private void RequestGetOffers() {
        final String username = ControladoraPresentacio.getUsername();
        final ArrayList<Offer> offer_list = new ArrayList<>();
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(Vendedor.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-offers?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject info_offer = response.getJSONObject(i);
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
                        Offer offer = new Offer(id, name, Integer.parseInt(category), type, keywords, Integer.parseInt(value), description, ControladoraPresentacio.getUsername());
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
        LinearLayout llBotonera = findViewById(R.id.listaOffers_sp);

        for (int i = 0; i < numBotones; ++i) {
            LinearLayout ll = new LinearLayout(Vendedor.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            //Definir que hay dentro del LinearLayout grande
            //Imagen Propiedad
            final ImageView foto = new ImageView(Vendedor.this);
            //Linear Layout VERTICAL con los datos (precio, nombre)
            LinearLayout ll_datos = new LinearLayout(Vendedor.this);
            ll_datos.setOrientation(LinearLayout.VERTICAL);
            TextView nom_producte = new TextView(Vendedor.this);
            TextView preu_producte = new TextView(Vendedor.this);
            TextView num_ofertes = new TextView(Vendedor.this);

            //Asignar valores a los elementos (una foto al imageview, texto al textView...)
            //foto.setImageURI();
            StorageReference Reference = storageRef.child("/products/" + offer_list.get(i).getId()).child("product_0");
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
            //Asignamos Texto a los textViews
            nom_producte.setText(offer_list.get(i).getName() + "");
            preu_producte.setText(offer_list.get(i).getValue() + "€");
            //TODO: Hay que cambiar esto por el Numero de Ofertas REAL por la Propiedad!
            num_ofertes.setText("Tienes: " +"X"+"ofertas");


            //Le damos el estilo que queremos al LinearLayout y a sus componentes
            ll.setBackgroundResource(R.drawable.button_rounded);
            preu_producte.setTextColor(Vendedor.this.getResources().getColor(R.color.colorLetraKatundu));
            num_ofertes.setTextColor(Vendedor.this.getResources().getColor(R.color.colorLetraKatundu));
            nom_producte.setTextColor(Vendedor.this.getResources().getColor(R.color.colorLetraKatundu));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            num_ofertes.setTypeface(boldTypeface);
            preu_producte.setTypeface(boldTypeface);
            nom_producte.setTypeface(boldTypeface);
            num_ofertes.setTextSize(18);
            preu_producte.setTextSize(18);
            nom_producte.setTextSize(18);

            //Asignar MARGENES
            //Margenes del layout dinamico
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsll.weight = 1;
            paramsll.height = 400;
            //paramsll.setMargins(left, top, right, bottom);
            paramsll.setMargins(0, 20, 0, 20);
            if (i == 0) paramsll.setMargins(0, 20, 0, 20);
            else paramsll.setMargins(0, 0, 0, 20);
            ll.setLayoutParams(paramsll);
            //Margenes de la ImageView
            //TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsFoto.weight = 1;
            paramsFoto.width = 300;
            paramsFoto.setMargins(25, 25, 25, 25);
            foto.setLayoutParams(paramsFoto);
            //Margenes del layout de datos
            //TableRow.LayoutParams paramsll_datos = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams paramsll_datos = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsll_datos.weight = 1;
            //paramsll_datos.height = 400;
            paramsll_datos.setMargins(25, 25, 25, 25);
            ll_datos.setLayoutParams(paramsll_datos);
            //Margenes de los datos
            TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsN.weight = 1;
            paramsN.setMargins(0, 10, 0, 10);
            nom_producte.setLayoutParams(paramsN);
            TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPrecio.setMargins(0, 10, 0, 20);
            preu_producte.setLayoutParams(paramsPrecio);
            TableRow.LayoutParams paramsNumOfertas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsNumOfertas.setMargins(0, 10, 0, 20);
            preu_producte.setLayoutParams(paramsNumOfertas);

            //Le escondemos el nombre del producto en la descripcion
            ll.setContentDescription(offer_list.get(i).getName());
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new Vendedor.LayoutOnClickListener(Vendedor.this));


            //Añadimos el layout dinamico al layout
            ll.addView(foto);
            ll_datos.addView(nom_producte);
            ll_datos.addView(preu_producte);
            ll_datos.addView(num_ofertes);
            ll.addView(ll_datos);
            llBotonera.addView(ll);
        }
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(Vendedor vendedor) {
        }

        @Override
        public void onClick(View view) {
            //Provando que funciona el layout en modo boton
            Toast.makeText(getApplicationContext(), view.getContentDescription().toString(), Toast.LENGTH_SHORT).show();

            //Pasamos los datos del deseo a la controladora
            Offer info_offer = ControladoraPresentacio.getOffer_perName(view.getContentDescription().toString());
            //Pasamos los datos del deseo a la controladora
            ControladoraPresentacio.setOffer_id(info_offer.getId());
            ControladoraPresentacio.setOffer_name(info_offer.getName());
            ControladoraPresentacio.setOffer_Categoria(info_offer.getCategory());
            boolean type = true;
            String tipus = info_offer.getType();
            if (tipus.equals("Producte")) type = false;
            ControladoraPresentacio.setOffer_Service(type);
            ControladoraPresentacio.setOffer_PC(info_offer.getKeywords());
            ControladoraPresentacio.setOffer_Value(info_offer.getValue());
            ControladoraPresentacio.setOffer_Description(info_offer.getDescription());
            //Nos vamos a la ventana de EditOffer
            Intent intent = new Intent(Vendedor.this, EditProperty.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
        }
    }
}