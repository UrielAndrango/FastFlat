package com.example.fatflat.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.Favorite;
import com.example.fatflat.ui.logged.Mapa;
import com.example.fatflat.ui.logged.MenuPrincipal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class RegisterHouseCharacteristics extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button[] btn_array = new Button[4];
    Button btn_unfocus;
    int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3};

    Button[] btn_array2 = new Button[3];
    Button btn_unfocus2;
    int[] btn_id2 = {R.id.btn4, R.id.btn5, R.id.btn6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_characteristics);
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.RegisterHouse_Atras);

        final Button SaveButton = findViewById(R.id.RegisterHouse_SaveButton);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterHouseCharacteristics.this, RegisterBuyerPreferences.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        for(int i = 0; i < btn_array.length; i++){
            btn_array[i] = (Button) findViewById(btn_id[i]);
            btn_array[i].setTextColor(getResources().getColor(R.color.colorLetraKatundu));
            btn_array[i].setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));
            btn_array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btn0 :
                            setFocus(btn_unfocus, btn_array[0]);
                            break;

                        case R.id.btn1 :
                            setFocus(btn_unfocus, btn_array[1]);
                            break;

                        case R.id.btn2 :
                            setFocus(btn_unfocus, btn_array[2]);
                            break;

                        case R.id.btn3 :
                            setFocus(btn_unfocus, btn_array[3]);
                            break;
                    }
                }
            });
        }
        btn_unfocus = btn_array[0];

        for(int i = 0; i < btn_array2.length; i++){
            btn_array2[i] = (Button) findViewById(btn_id2[i]);
            btn_array2[i].setTextColor(getResources().getColor(R.color.colorLetraKatundu));
            btn_array2[i].setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));
            btn_array2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btn4 :
                            setFocus2(btn_unfocus2, btn_array2[0]);
                            break;

                        case R.id.btn5 :
                            setFocus2(btn_unfocus2, btn_array2[1]);
                            break;

                        case R.id.btn6 :
                            setFocus2(btn_unfocus2, btn_array2[2]);
                            break;
                    }
                }
            });
        }

        btn_unfocus2 = btn_array2[0];

        radioGroup = findViewById(R.id.radioGroup);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner3 = findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.state_wo_indifferent, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);
/*
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterHouseCharacteristics.this, MenuPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
*/
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //desactivar login momentaneamente
                SaveButton.setEnabled(false);
                ControladoraPresentacio.setUsername("UrielAndrango");
                RequestLogin(SaveButton);
            }
        });
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(getResources().getColor(R.color.colorLetraKatundu));
        btn_unfocus.setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));
        btn_focus.setTextColor(getResources().getColor(R.color.colorBlancoMate));
        btn_focus.setBackgroundColor(getResources().getColor(R.color.color_focused));
        this.btn_unfocus = btn_focus;
    }

    private void setFocus2(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(getResources().getColor(R.color.colorLetraKatundu));
        btn_unfocus.setBackgroundColor(getResources().getColor(R.color.colorBlancoMate));
        btn_focus.setTextColor(getResources().getColor(R.color.colorBlancoMate));
        btn_focus.setBackgroundColor(getResources().getColor(R.color.color_focused));
        this.btn_unfocus2 = btn_focus;
    }

    private void RequestLogin(final Button login_button) {
        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(RegisterHouseCharacteristics.this);

        final String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-login?" +
                "un=" + "UrielAndrango" + "&" +
                "pw=" + "1";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Successful login
                            //Inicialitzem amb les dades de l'usuari
                            RequestInicialitzaDadesUsuari("UrielAndrango", queue, login_button);
                            manage_notifications("UrielAndrango");
                        }
                        else if(response.equals("2")){ //Incorrect password
                            ControladoraPresentacio.setIntentosLogin(ControladoraPresentacio.getIntentosLogin() + 1); //incrementem els intents que hem fet
                            String texterror = getString(R.string.incorrect_password);
                            Toast toast = Toast.makeText(RegisterHouseCharacteristics.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                        }
                        else if(response.equals("1")) { //No such user!
                            String texterror = getString(R.string.unregistered);
                            Toast toast = Toast.makeText(RegisterHouseCharacteristics.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                        }
                        else { //response == "-1" Error getting document + err
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(RegisterHouseCharacteristics.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //Reactivar login
                            login_button.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(RegisterHouseCharacteristics.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                //Reactivar login
                login_button.setEnabled(true);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RequestInicialitzaDadesUsuari(final String username, RequestQueue queue, final Button login_button) {
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

        final RequestQueue queue = Volley.newRequestQueue(RegisterHouseCharacteristics.this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}