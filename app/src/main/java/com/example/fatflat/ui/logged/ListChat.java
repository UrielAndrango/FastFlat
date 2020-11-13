
package com.example.fatflat.ui.logged;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListChat extends AppCompatActivity {

    SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.ListChat_Atras);
        final LinearLayout llBotonera = findViewById(R.id.LinearLayout_Chats);

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
        RequestGetChats(llBotonera);
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
    }

    private void RequestGetChats(final LinearLayout llBotonera) {
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
                    InicialitzaBotons(chats_list, llBotonera);
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
    private void InicialitzaBotons(ArrayList<Pair<String,String>> chats_list, LinearLayout llBotonera) {
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
