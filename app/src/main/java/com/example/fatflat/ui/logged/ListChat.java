
package com.example.fatflat.ui.logged;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListChat extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SwipeRefreshLayout refreshLayout;

    LinearLayout llBotonera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.ListChat_Atras);
        llBotonera = findViewById(R.id.LinearLayout_Chats);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListChat.this, MenuPrincipal.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout_LC);
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

        RequestGetChats();
    }

    private void RequestGetChats() {
        final String username = ControladoraPresentacio.getUsername();
        // Instantiate the RequestQueue.
        final ArrayList<Pair<String,String>> chats_list = new ArrayList<>();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(ListChat.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-chats?" + "un=" + username;
        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_message = response.getJSONObject(i);
                        String user = info_message.getString("user");
                        String id_chat = info_message.getString("id");
                        Pair<String, String> info = new Pair(user,id_chat);
                        chats_list.add(info);
                    }
                    //InicialitzaBotons(chats_list);
                    InicialitzaBotonsOffers(chats_list);
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
    private void InicialitzaBotonsOffers(ArrayList<Pair<String,String>> chat_list) {
        //Borramos la busqueda anterior
        //if (llBotonera.getChildCount() > 0) llBotonera.removeAllViews();

        //ControladoraPresentacio.setOffer_List(chat_list);
        int numBotones = chat_list.size();

        //Obtenemos el linear layout donde colocar los botones
        //LinearLayout llBotonera = findViewById(R.id.listaPropiedades_MyP);

        for (int i=0; i<numBotones; ++i) {
            //Barrita separadora
            if (i>0) {
                LinearLayout Barrita = new LinearLayout(ListChat.this);
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

            LinearLayout ll = new LinearLayout(ListChat.this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            //Definir que hay dentro del LinearLayout grande
            //Imagen Propiedad
            final ImageView foto = new ImageView(ListChat.this);
            //Linear Layout VERTICAL con los datos (precio, nombre)
            LinearLayout ll_datos = new LinearLayout(ListChat.this);
            ll_datos.setOrientation(LinearLayout.VERTICAL);
            TextView nom_producte = new TextView(ListChat.this);
            TextView preu_producte = new TextView(ListChat.this);

            //Asignar valores a los elementos (una foto al imageview, texto al textView...)
            //foto.setImageURI();
            /*
            StorageReference Reference = storageRef.child("/products/" + chat_list.get(i).getId()).child("product_0");
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
            */
            Drawable drawable = getResources().getDrawable(R.drawable.icon_trophy_500);
            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
            //Redondeamos las esquinas de las fotos
            bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2*8);
            foto.setImageBitmap(bmp);
            foto.setVisibility(View.VISIBLE);
            //Asignamos Texto a los textViews
            nom_producte.setText(chat_list.get(i).first + "");
            //preu_producte.setText(chat_list.get(i).getValue() + "€");
            //TODO: Hay que cambiar esto por el Nombre de la Propiedad Real!
            preu_producte.setText("PROPIEDAD "+(i+1));


            //Le damos el estilo que queremos al LinearLayout y a sus componentes
            //ll.setBackgroundResource(R.drawable.button_rounded);
            preu_producte.setTextColor(ListChat.this.getResources().getColor(R.color.colorBlancoMate));
            nom_producte.setTextColor(ListChat.this.getResources().getColor(R.color.colorBlancoMate));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            preu_producte.setTypeface(boldTypeface);
            nom_producte.setTypeface(boldTypeface);
            preu_producte.setTextSize(16);
            nom_producte.setTextSize(20);

            //Asignar MARGENES
            //Margenes del layout dinamico
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsll.weight = 1;
            paramsll.height = 200;
            //paramsll.setMargins(left, top, right, bottom);
            paramsll.setMargins(0, 20, 0, 20);
            if (i==0) paramsll.setMargins(0, 20, 0, 20);
            else paramsll.setMargins(0, 0, 0, 20);
            ll.setLayoutParams(paramsll);
            //Margenes de la ImageView
            //TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsFoto.weight = 1;
            paramsFoto.width = 150;
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
            paramsN.setMargins(0, 0, 0, 10);
            nom_producte.setLayoutParams(paramsN);
            TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPrecio.setMargins(0, 10, 0, 0);
            preu_producte.setLayoutParams(paramsPrecio);

            //Le escondemos el nombre del producto en la descripcion
            //ll.setContentDescription(chat_list.get(i).getName());
            ll.setContentDescription(chat_list.get(i).first);
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new ListChat.LayoutOnClickListener(ListChat.this));


            //Añadimos el layout dinamico al layout
            ll.addView(foto);
            ll_datos.addView(nom_producte);
            ll_datos.addView(preu_producte);
            ll.addView(ll_datos);
            llBotonera.addView(ll);
        }
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(ListChat listChat) {
        }
        @Override
        public void onClick(final View view) {
            //Provando que funciona el layout en modo boton
            Toast.makeText(getApplicationContext(),view.getContentDescription().toString(),Toast.LENGTH_SHORT).show();
/*
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
            Intent intent = new Intent(ListChat.this, EditProperty.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            //finish();
*/
            final RequestQueue queue = Volley.newRequestQueue(ListChat.this);

            String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-getID?" + "un1=" + ControladoraPresentacio.getUsername()+ "&un2=" + view.getContentDescription().toString();
            // Request a JSONObject response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ControladoraChat.setUsername1(ControladoraPresentacio.getUsername());
                            //ControladoraChat.setUsername2(view.getContentDescription().toString());
                            ControladoraChat.setUsername2(view.getContentDescription().toString());
                            ControladoraChat.setId_Chat(response);
                            //Nos vamos a la ventana de EditOffer
                            Intent intent = new Intent(ListChat.this, VisualizeChat.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {  //TODO: aixo ho podem treure?
                    String texterror = getString(R.string.error);
                    Toast toast = Toast.makeText(ListChat.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                    //reactivar resgistrarse
                }
            });
            queue.add(stringRequest);
        }
    }

    private void InicialitzaBotons(ArrayList<Pair<String,String>> chats_list) {
            //Borramos la busqueda anterior
            if (llBotonera.getChildCount() > 0) llBotonera.removeAllViews();

            /* Creación de la lista mensajes */
            //Esto es temporal, hay que hacer tanto botones como usuarios hagan match en la busqueda
            int numBotones =chats_list.size();

            //Creamos las propiedades de layout que tendrán los botones.
            //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT );

            //Creamos los botones en bucle
            for (int i=0; i<numBotones; i++){
                //Modo Layout con 2 TextViews
                Button ll = new Button(ListChat.this);

                //Asignamos propiedades de layout al layout
                ll.setLayoutParams(lp);

                //Le damos el estilo que queremos
                ll.setBackgroundResource(R.drawable.button_rounded);
                ll.setTextColor(this.getResources().getColor(R.color.colorLetraKatundu));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                if(i == 0) params.setMargins(0, 20, 0, 20);
                else params.setMargins(0, 0, 0, 20);
                ll.setLayoutParams(params);
                //Añadimos el layout dinamico al layout
                ll.setText(chats_list.get(i).first);
                ll.setContentDescription(chats_list.get(i).first);
                ll.setOnClickListener(new ListChat.ButtonOnClickListener(ListChat.this));
                llBotonera.addView(ll);
                llBotonera.setOnClickListener(new ListChat.ButtonOnClickListener(ListChat.this));
            }
    }
    private class ButtonOnClickListener implements View.OnClickListener {
        public ButtonOnClickListener(ListChat listchat) {
        }
        @Override
        public void onClick(final View view) {
            //System.out.println("1");
            //Provando que funciona el layout en modo boton
            Button b = (Button) view;
            Toast.makeText(getApplicationContext(),view.getContentDescription().toString(),Toast.LENGTH_SHORT).show();
            //PROVA
            final RequestQueue queue = Volley.newRequestQueue(ListChat.this);

            String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-getID?" + "un1=" + ControladoraPresentacio.getUsername()+ "&un2=" + b.getText().toString();
            // Request a JSONObject response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ControladoraChat.setUsername1(ControladoraPresentacio.getUsername());
                            ControladoraChat.setUsername2(view.getContentDescription().toString());
                            ControladoraChat.setId_Chat(response);
                            //Nos vamos a la ventana de EditOffer
                            Intent intent = new Intent(ListChat.this, VisualizeChat.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            //finish();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {  //TODO: aixo ho podem treure?
                    String texterror = getString(R.string.error);
                    Toast toast = Toast.makeText(ListChat.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                    //reactivar resgistrarse
                }
            });
            queue.add(stringRequest);
        }
    }
}
