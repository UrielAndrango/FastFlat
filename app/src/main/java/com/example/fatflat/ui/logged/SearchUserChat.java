
package com.example.fatflat.ui.logged;

        import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
        import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.ControladoraSearchUsers;

        import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchUserChat extends AppCompatActivity {

    //Obtenemos el linear layout donde colocar los botones
    LinearLayout llBotonera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_chat);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.Search_Chat_Atras);
        final SearchView search = findViewById(R.id.search_users);
        //Obtenemos el linear layout donde colocar los botones
        llBotonera = (LinearLayout) findViewById(R.id.listaUsers_SU);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchUserChat.this, ListChat.class);
                onNewIntent(intent);
                finish();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                RequestSearchUser(search.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void RequestSearchUser(final String username) {
        final RequestQueue queue = Volley.newRequestQueue(SearchUserChat.this);
        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-search?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    ControladoraSearchUsers.setUsername(username);
                    ControladoraSearchUsers.setNombre_real(response.getString("name"));
                    //ControladoraSearchUsers.setValoracion();

                    String welcome = getString(R.string.search_user_good_search);
                    Toast toast = Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT);
                    toast.show();
                    InicialitzarBotons(username);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String welcome = getString(R.string.search_user_bad_search);
                    Toast toast = Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
            }
        });
        queue.add(jsObjectRequest);
    }

    private void InicialitzarBotons(String username) {
        //Borramos la busqueda anterior
        if (llBotonera.getChildCount() > 0) llBotonera.removeAllViews();

        /* Creación de la LISTA DE WISHES */
        //Esto es temporal, hay que hacer tanto botones como usuarios hagan match en la busqueda
        int numBotones = 1;
        //Obtenemos el linear layout donde colocar los botones
        //LinearLayout llBotonera = (LinearLayout) findViewById(R.id.listaUsers_SU);
        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        //Creamos los botones en bucle
        for (int i=0; i<numBotones; i++){
            //Modo Layout con 2 TextViews
            LinearLayout ll = new LinearLayout(SearchUserChat.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView text_username = new TextView(SearchUserChat.this);
            TextView text_realName = new TextView(SearchUserChat.this);
            //Asignamos propiedades de layout al layout
            ll.setLayoutParams(lp);
            //Asignamos Texto a los textViews
            text_username.setText(getString(R.string.search_user_username) + ": " + username);
            text_realName.setText(getString(R.string.search_user_real_name) + ": " + ControladoraSearchUsers.getNombre_real());
            //Le damos el estilo que queremos
            ll.setBackgroundResource(R.drawable.button_rounded);
            text_username.setTextColor(SearchUserChat.this.getResources().getColor(R.color.colorLetraKatundu));
            text_realName.setTextColor(SearchUserChat.this.getResources().getColor(R.color.colorLetraKatundu));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            text_username.setTypeface(boldTypeface);
            text_realName.setTypeface(boldTypeface);
            text_username.setTextSize(18);
            text_realName.setTextSize(18);
            //Margenes del layout
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsll.setMargins(left, top, right, bottom);
            if (i==0) paramsll.setMargins(0, 20, 0, 20);
            else paramsll.setMargins(0, 0, 0, 20);
            ll.setLayoutParams(paramsll);
            //Margenes de los textViews
            TableRow.LayoutParams paramsU = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsU.setMargins(25, 20, 25, 10);
            text_username.setLayoutParams(paramsU);
            TableRow.LayoutParams paramsRN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsRN.setMargins(25, 10, 25, 10);
            text_realName.setLayoutParams(paramsRN);
            TableRow.LayoutParams paramsV = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsV.setMargins(25, 10, 25, 20);
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new LayoutOnClickListener(SearchUserChat.this));
            //Añadimos el layout dinamico al layout
            ll.addView(text_username);
            ll.addView(text_realName);
            ll.setContentDescription(username);
            llBotonera.addView(ll);
        }
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(SearchUserChat searchUser) {
        }
        @Override
        public void onClick(View view) {
            //Pasamos los datos del deseo a la controladora
            //TODO: Se debe pedir las listas del usuario que se ha clicado

            // Instantiate the RequestQueue.
            final RequestQueue queue = Volley.newRequestQueue(SearchUserChat.this);

            String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-create?" + "un1=" + ControladoraPresentacio.getUsername() + "&un2=" +  view.getContentDescription().toString();

            // Request a JSONObject response from the provided URL.
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
                }
            });
            queue.add(jsonArrayRequest);
            //Nos vamos a la ventana de VisualizeListOUser
            Intent intent = new Intent(SearchUserChat.this, ListChat.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
