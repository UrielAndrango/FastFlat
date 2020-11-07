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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraChat;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.Favorite;
import com.example.katundu.ui.logged.MenuPrincipal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;//TODO:Esto se puede quitar?? No se para que sirve...
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( ControladoraPresentacio.getUsername() != "null")
        {
            Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_login);
            //Detectar idioma
            loadLocale();
            //Oculta la barra que dice el nombre de la apliacion en la Action Bar (asi de momento)
            getSupportActionBar().hide();

            //TODO:Esto se puede quitar??
            //Firebase Anlytics de prueba
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mAuth = FirebaseAuth.getInstance();

            final EditText usernameEditText = findViewById(R.id.username);
            final EditText passwordEditText = findViewById(R.id.passwordEdit);
            final Button login_button = findViewById(R.id.login);
            final TextView no_registrado = findViewById(R.id.no_registrado);

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //desactivar login momentaneamente
                    login_button.setEnabled(false);
                    no_registrado.setEnabled(false);
                    if (ControladoraPresentacio.getIntentosLogin() < 4)
                        RequestLogin(usernameEditText, passwordEditText, login_button, no_registrado);
                    else {
                        ControladoraPresentacio.setIntentosLogin(0);
                        ControladoraPresentacio.setUsername(usernameEditText.getText().toString());
                        RequestQuestion();
                    }
                }
            });

            no_registrado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    //finish();
                }
            });

            //TODO: Esto se puede quitar??
            Bundle bundle = new Bundle();
            //bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
            //bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    private void RequestQuestion() {
        final RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-SecurityQuestion?un=" + ControladoraPresentacio.getUsername();

        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //question = response.getString("question");
                    ControladoraPresentacio.setQuestion(response.getString("question"));
                    //answer = response.getString("answer");
                    ControladoraPresentacio.setAnswer(response.getString("answer"));
                    System.out.println("Q " + response.getString("question"));
                    System.out.println("A " + response.getString("answer"));
                    Intent intent = new Intent(LoginActivity.this, SecurityLogin.class);
                    startActivity(intent);
                    finish();

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
        jsObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(jsObjectRequest);
    }

    private void RequestLogin(final EditText usernameEditText, EditText passwordEditText, final Button login_button, final TextView no_registrado) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        final String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-login?" +
                "un=" + usernameEditText.getText() + "&" +
                "pw=" + passwordEditText.getText();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Successful login
                            //Inicialitzem amb les dades de l'usuari
                            RequestInicialitzaDadesUsuari(usernameEditText.getText().toString(), queue, login_button, no_registrado);
                            manage_notifications(usernameEditText.getText().toString());
                        }
                        else if(response.equals("2")){ //Incorrect password
                            ControladoraPresentacio.setIntentosLogin(ControladoraPresentacio.getIntentosLogin() + 1); //incrementem els intents que hem fet
                            String texterror = getString(R.string.incorrect_password);
                            Toast toast = Toast.makeText(LoginActivity.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                            no_registrado.setEnabled(true);
                        }
                        else if(response.equals("1")) { //No such user!
                            String texterror = getString(R.string.unregistered);
                            Toast toast = Toast.makeText(LoginActivity.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                            no_registrado.setEnabled(true);
                        }
                        else { //response == "-1" Error getting document + err
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(LoginActivity.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                            no_registrado.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(LoginActivity.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                //Reactivar login
                login_button.setEnabled(true);
                no_registrado.setEnabled(true);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RequestInicialitzaDadesUsuari(final String username, RequestQueue queue, final Button login_button, final TextView no_registrado) {
        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-infoUser?" + "un=" + username;

        // Request a JSONObject response from the provided URL.
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    ControladoraPresentacio.setUsername(username);
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

                    //Canviem de pantalla i anem al Menu Principal
                    Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                    String welcome = getString(R.string.welcome) + username;
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
                login_button.setEnabled(true);
                no_registrado.setEnabled(true);
            }
        });
        queue.add(jsObjectRequest);
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

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String idioma = prefs.getString("My_Lang", "");
        setLocale(idioma);
    }

    private void manage_notifications(final String username){

        final RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-users";

        // Request a JSONObject response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info = response.getJSONObject(i);
                        String user = info.getString("username");
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });

                    }
                    FirebaseMessaging.getInstance().subscribeToTopic(username)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });
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
}