package com.example.katundu.ui.logged;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraChat;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class VisualizeChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_chat);

        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final String username1 = ControladoraPresentacio.getUsername();
        final TextView username2 = findViewById(R.id.nomUsuari);
        final ImageView Atras = findViewById(R.id.ViewChat_Atras);
        final String id_Chat = ControladoraChat.getId_Chat(); 
        final ImageView Enviar_message = findViewById(R.id.enviar_message);
        final EditText contingut_message = findViewById(R.id.contingut_message);
        final LinearLayout llBotonera = findViewById(R.id.LinearLayout_Messages);
        final ScrollView scrollView = findViewById(R.id.scrollview);
        final ImageView refresh = findViewById(R.id.icono_refresh);
        final ImageView end_exchange = findViewById(R.id.icono_end_exchange);

        username2.setText(ControladoraChat.getUsername2());

        RequestGetMessages(id_Chat, llBotonera, scrollView);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeChat.this, ListChat.class);
                onNewIntent(intent);
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        Enviar_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contingut_message.getText().toString().trim().length() != 0) {
                    Enviar_message.setEnabled(false);
                    RequestAddMessage(id_Chat, username1, contingut_message, llBotonera, scrollView, Enviar_message);
                }
            }
        });

        end_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeChat.this, EndExchange.class);
                startActivity(intent);
                //finish();
            }
        });

        /* PER SI VOLEM FER UNA REQUEST DE GETMESSAGES CADA X TEMPS
        //Fa la request de getMessages cada 10 segons per mantenir actualitzats els missatges del chat
        final Handler handler = new Handler();

        final int TIEMPO = 10000;

        handler.postDelayed(new Runnable() {
            public void run() {
                RequestGetMessages(id_Chat, llBotonera, scrollView);
                handler.postDelayed(this, TIEMPO);
            }
        }, TIEMPO);
         */
    }

    private void RequestAddMessage(final String id_Chat, String username1, final TextView contingut_message, final LinearLayout llBotonera, final ScrollView scrollView, final ImageView Enviar_message) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(VisualizeChat.this);

        String contingut_message_transformat = contingut_message.getText().toString();
        contingut_message_transformat = contingut_message_transformat.replaceAll(System.getProperty("line.separator"), "%0A");

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-addMessage?" +
                "id=" + id_Chat + "&" +
                "un=" + username1 + "&" +
                "message=" + contingut_message_transformat;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //message added successfully
                            contingut_message.setText("");
                            contingut_message.setHint(getString(R.string.contingut_message));
                            Enviar_message.setEnabled(true);

                            try {
                                Thread.sleep(5*1000); //Ho faig per deixar algo de temps a Firebase per a que fagi el addMessage
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            RequestGetMessages(id_Chat, llBotonera, scrollView);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(VisualizeChat.this, texterror, Toast.LENGTH_SHORT);
                toast.show();

                Enviar_message.setEnabled(true);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RequestGetMessages(String id_Chat, final LinearLayout llBotonera, final ScrollView scrollView) {
        final ArrayList<Message> message_list = new ArrayList<>();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(VisualizeChat.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-getMessages?" + "id=" + id_Chat;

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info_message = response.getJSONObject(i);

                        String username = info_message.getString("username");
                        String contingut_message = info_message.getString("message");
                        String time = info_message.getString("time");

                        Message message = new Message(username, contingut_message, time);

                        message_list.add(message);
                    }

                    InicialitzarLayoutsMissatges(llBotonera, message_list, scrollView);

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void InicialitzarLayoutsMissatges(LinearLayout llBotonera, ArrayList<Message> message_list, final ScrollView scrollView) {
        //Borramos la busqueda anterior
        if (llBotonera.getChildCount() > 0) llBotonera.removeAllViews();

        /* Creación de la lista mensajes */
        //Esto es temporal, hay que hacer tanto botones como usuarios hagan match en la busqueda
        int numBotones = message_list.size();

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        //Creamos los botones en bucle
        for (int i=0; i<numBotones; i++){
            //Modo Layout con 2 TextViews
            LinearLayout ll = new LinearLayout(VisualizeChat.this);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView text_missatge = new TextView(VisualizeChat.this);
            TextView time_missatge = new TextView(VisualizeChat.this);

            //Asignamos propiedades de layout al layout
            ll.setLayoutParams(lp);

            //Asignamos Texto a los textViews
            text_missatge.setText(message_list.get(i).getMessage());

            String string = message_list.get(i).getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

            String date = LocalDate.parse(string, formatter).format(DateTimeFormatter.ISO_DATE);
            String temps = LocalTime.parse(string, formatter).format(DateTimeFormatter.ISO_TIME);

            temps = temps.substring(0,5);

            String contingut_temps = date + " " + temps;

            time_missatge.setText(contingut_temps);

            //Le damos el estilo que queremos
            if(message_list.get(i).getUsername().equals(ControladoraPresentacio.getUsername()))
                ll.setBackgroundResource(R.drawable.button_rounded_message);
            else ll.setBackgroundResource(R.drawable.button_rounded);

            text_missatge.setTextColor(VisualizeChat.this.getResources().getColor(R.color.colorLetraKatundu));
            text_missatge.setTextSize(18);

            time_missatge.setTextColor(VisualizeChat.this.getResources().getColor(R.color.colorLetraKatundu));
            time_missatge.setTextSize(10);

            //Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            //text_missatge.setTypeface(boldTypeface);

            //Margenes del layout
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsll.setMargins(left, top, right, bottom);
            if(message_list.get(i).getUsername().equals(ControladoraPresentacio.getUsername())) {
                if (i == 0) paramsll.setMargins(300, 20, 0, 20);
                else paramsll.setMargins(300, 0, 0, 20);
            }
            else {
                if (i == 0) paramsll.setMargins(0, 20, 300, 20);
                else paramsll.setMargins(0, 0, 300, 20);
            }

            ll.setLayoutParams(paramsll);

            //Margenes del textView
            TableRow.LayoutParams paramsU = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            paramsU.setMargins(25, 20, 25, 10);

            text_missatge.setLayoutParams(paramsU);
            time_missatge.setLayoutParams(paramsU);

            if(message_list.get(i).getUsername().equals(ControladoraPresentacio.getUsername())) {
                text_missatge.setGravity(Gravity.START);
                time_missatge.setGravity(Gravity.END);
            }
            else {
                text_missatge.setGravity(Gravity.START);
                time_missatge.setGravity(Gravity.START);
            }

            //Asignamose el Listener al Layout dinamico
            //ll.setOnClickListener(new SearchUser.LayoutOnClickListener(SearchUser.this));

            //Añadimos el layout dinamico al layout
            ll.addView(text_missatge);
            ll.addView(time_missatge);

            llBotonera.addView(ll);

            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
