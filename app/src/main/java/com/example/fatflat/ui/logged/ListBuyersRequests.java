package com.example.fatflat.ui.logged;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.Offer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListBuyersRequests extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SwipeRefreshLayout refreshLayout;
    LinearLayout llBotonera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buyers_requests);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.ListBuyersRequests_Atras);
        llBotonera = findViewById(R.id.LinearLayout_LBR);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListBuyersRequests.this, Vendedor.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout_LBR);
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
        final RequestQueue queue = Volley.newRequestQueue(ListBuyersRequests.this);

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
        //Borramos la busqueda anterior
        //if (llBotonera.getChildCount() > 0) llBotonera.removeAllViews();

        ControladoraPresentacio.setOffer_List(offer_list);
        int numBotones = offer_list.size();

        //Obtenemos el linear layout donde colocar los botones
        //LinearLayout llBotonera = findViewById(R.id.listaPropiedades_MyP);

        for (int i=0; i<numBotones; ++i) {
            //Barrita separadora
            if (i>0) {
                LinearLayout Barrita = new LinearLayout(ListBuyersRequests.this);
                Barrita.setOrientation(LinearLayout.HORIZONTAL);

                Barrita.setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));

                //Margenes del layout dinamico
                TableRow.LayoutParams paramsBarrita = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                //paramsBarrita.weight = 1;
                paramsBarrita.height = 1;
                //paramsll.setMargins(left, top, right, bottom);
                paramsBarrita.setMargins(0, 0, 0, 0);
                Barrita.setLayoutParams(paramsBarrita);

                //Agregamos al Linear Layout grande
                llBotonera.addView(Barrita);
            }

            LinearLayout ll = new LinearLayout(ListBuyersRequests.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            //Definir que hay dentro del LinearLayout grande
            //Imagen Propiedad
            final CircleImageView foto = new CircleImageView(ListBuyersRequests.this);
            //Linear Layout VERTICAL con los datos (precio, nombre)
            LinearLayout ll_datos = new LinearLayout(ListBuyersRequests.this);
            ll_datos.setOrientation(LinearLayout.VERTICAL);
            TextView nom_producte = new TextView(ListBuyersRequests.this);
            TextView preu_producte = new TextView(ListBuyersRequests.this);

            //Asignar valores a los elementos (una foto al imageview, texto al textView...)
            //foto.setImageURI();

            StorageReference Reference = storageRef.child("/products/" + offer_list.get(i).getId()).child("product_0");
            Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    //bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2*8);
                    foto.setImageBitmap(bmp);
                    foto.setVisibility(View.VISIBLE);
                }
            });
            /*
            Drawable drawable = getResources().getDrawable(R.drawable.icon_trophy_500);
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            //Redondeamos las esquinas de las fotos
            bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2*8);
            foto.setImageBitmap(bmp);
            foto.setVisibility(View.VISIBLE);
            */
            //Asignamos Texto a los textViews
            nom_producte.setText(offer_list.get(i).getName() + "");
            //preu_producte.setText(chat_list.get(i).getValue() + "€");
            //TODO: Hay que cambiar esto por el Nombre de la Propiedad Real!
            preu_producte.setText("PROPIEDAD "+(i+1));


            //Le damos el estilo que queremos al LinearLayout y a sus componentes
            //ll.setBackgroundResource(R.drawable.button_rounded);
            preu_producte.setTextColor(ListBuyersRequests.this.getResources().getColor(R.color.colorBlancoMate));
            nom_producte.setTextColor(ListBuyersRequests.this.getResources().getColor(R.color.colorBlancoMate));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            preu_producte.setTypeface(boldTypeface);
            nom_producte.setTypeface(boldTypeface);
            preu_producte.setTextSize(14);
            nom_producte.setTextSize(18);

            //Asignar MARGENES
            //Margenes del layout dinamico
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsll.weight = 1;
            //paramsll.height = 200;
            //paramsll.setMargins(left, top, right, bottom);
            paramsll.setMargins(0, 20, 0, 20);
            if (i==0) paramsll.setMargins(35, 10, 25, 10);
            else paramsll.setMargins(35, 10, 25, 10);
            ll.setLayoutParams(paramsll);
            //Margenes de la ImageView
            //TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsFoto.weight = 1;
            paramsFoto.width = 150;
            paramsFoto.height = 150;
            paramsFoto.setMargins(10, 10, 10, 10);
            foto.setLayoutParams(paramsFoto);
            //Borde del circulo (aura)
            foto.setBorderWidth(2);
            foto.setBorderColor(getResources().getColor(R.color.colorBlancoMate));
            //Margenes del layout de datos
            //TableRow.LayoutParams paramsll_datos = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams paramsll_datos = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            paramsll_datos.weight = 1;
            //paramsll_datos.height = 400;
            paramsll_datos.setMargins(25, 10, 0, 10);
            ll_datos.setLayoutParams(paramsll_datos);
            //Margenes de los datos
            TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsN.weight = 1;
            paramsN.setMargins(0, 0, 0, 10);
            nom_producte.setLayoutParams(paramsN);
            TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPrecio.setMargins(0, 10, 0, 0);
            preu_producte.setLayoutParams(paramsPrecio);

            //Le escondemos el nombre del producto en la descripcion
            ll.setContentDescription(offer_list.get(i).getName());
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new LayoutOnClickListener(ListBuyersRequests.this));


            //Añadimos el layout dinamico al layout
            ll.addView(foto);
            ll_datos.addView(nom_producte);
            ll_datos.addView(preu_producte);
            ll.addView(ll_datos);
            llBotonera.addView(ll);
        }
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(ListBuyersRequests listB_ByP) {
        }
        @Override
        public void onClick(final View view) {
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
            Intent intent = new Intent(ListBuyersRequests.this, BuyerRequest.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
        }
    }
}