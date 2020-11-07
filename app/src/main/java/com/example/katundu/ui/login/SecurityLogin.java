package com.example.katundu.ui.login;

import android.app.Activity;
import android.app.AutomaticZenRule;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.Favorite;
import com.example.katundu.ui.logged.MenuPrincipal;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SecurityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_login);
        //Detectar idioma
        loadLocale();
        //Oculta la barra que dice el nombre de la apliacion en la Action Bar (asi de momento)
        getSupportActionBar().hide();

        final TextView questionEditText = findViewById(R.id.questionText);
        final EditText answerEditText = findViewById(R.id.answerEditText);
        final TextView envirButton = findViewById(R.id.enviarButton);

        questionEditText.setText(ControladoraPresentacio.getQuestion());

        envirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ControladoraPresentacio.getIntentosSecurityLogin() < 4) { //queden intents

                    if (!answerEditText.getText().toString().equals(ControladoraPresentacio.getAnswer())) { //resposta incorrecta
                        ControladoraPresentacio.setIntentosSecurityLogin(ControladoraPresentacio.getIntentosSecurityLogin() + 1);
                        String wrong_answer = getString(R.string.wrong_answer);
                        Toast toast = Toast.makeText(getApplicationContext(), wrong_answer, Toast.LENGTH_SHORT);
                        toast.show();
                    } else { //resposta correcta
                        envirButton.setEnabled(false);
                        RequestIniciarSessioIDades(envirButton);
                    }
                }
                else { //bloquejar compte
                    envirButton.setEnabled(false);
                    RequestBlockAccount();
                }
            }
        });

    }

    private void RequestIniciarSessioIDades(final TextView envirButton) {
        final RequestQueue queue = Volley.newRequestQueue(SecurityLogin.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-infoUser?" + "un=" + ControladoraPresentacio.getUsername();

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ControladoraPresentacio.setUsername(ControladoraPresentacio.getUsername());
                    ControladoraPresentacio.setNom_real(response.getString("name"));
                    ControladoraPresentacio.setPassword(response.getString("password"));
                    ControladoraPresentacio.setLatitud(response.getString("latitud"));
                    ControladoraPresentacio.setLongitud(response.getString("longitud"));
                    ControladoraPresentacio.setDistanciaMaxima(response.getString("distanciamaxima"));
                    ControladoraPresentacio.setValoracion(Double.parseDouble(response.getString("valoracio")));

                    JSONArray favorite_array = response.getJSONArray("favorite");
                    ArrayList<Favorite> favorites_user = new ArrayList<>();
                    for(int i = 0; i < favorite_array.length(); ++i) {
                        Favorite favorite = new Favorite();
                        favorite.setId(favorite_array.getString(i));
                        favorites_user.add(favorite);
                    }
                    ControladoraPresentacio.setFavorite_List(favorites_user);

                    ControladoraPresentacio.setIntentosSecurityLogin(0);
                    //Canviem de pantalla i anem al Menu Principal
                    Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                    startActivity(intent);
                    finish();

                    String welcome = getString(R.string.welcome) + ControladoraPresentacio.getUsername();
                    Toast toast = Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
                envirButton.setEnabled(true);
            }
        });
        queue.add(jsObjectRequest);
    }

    private void RequestBlockAccount() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(SecurityLogin.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-delete?" +
                "un=" + ControladoraPresentacio.getUsername();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account deleted successfully
                            String account_blocked_successfully = getString(R.string.account_blocked_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), account_blocked_successfully, Toast.LENGTH_SHORT);
                            toast.show();
                            ControladoraPresentacio.setUsername("null");
                            Intent intent = new Intent(SecurityLogin.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                        }
                        else { //response == "1" Something went wrong
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(SecurityLogin.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            final TextView envirButton = findViewById(R.id.enviarButton);
                            envirButton.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(SecurityLogin.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String idioma = prefs.getString("My_Lang", "");
        setLocale(idioma);
    }

    private void setLocale(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", idioma);
        editor.apply();
    }
}
