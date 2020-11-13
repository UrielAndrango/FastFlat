package com.example.fatflat.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraChat;

public class RateExchange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_rate_exchange);


        final ImageView Atras = findViewById(R.id.EndE_Atras);
        final EditText ratingEditText = findViewById(R.id.ratingEditText);
        final TextView envirButton = findViewById(R.id.enviarButton);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RateExchange.this, EndExchange.class);
                onNewIntent(intent);
                finish();
            }
        });

        envirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = ratingEditText.getText().toString();
                float v = Float.parseFloat(rating);
                System.out.println("valoracio " + v);
                if (v < 1 || v > 5) {
                    String wrong_answer = getString(R.string.wrong_rating);
                    Toast toast = Toast.makeText(getApplicationContext(), wrong_answer, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    RequestValorar(v);
                    Intent intent = new Intent(RateExchange.this, ListChat.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void RequestValorar(float v) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(RateExchange.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/add-valoracio?" +
                "user=" + ControladoraChat.getUsername2() + "&" +
                "punt=" + v;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //message added successfully
                            Intent intent = new Intent(RateExchange.this, ListChat.class);
                            startActivity(intent);
                            finish();
                        }
                        else { //algun error (?)
                            String texterror = getString(R.string.add_post_general_error);
                            Toast toast = Toast.makeText(RateExchange.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.add_post_general_error);
                Toast toast = Toast.makeText(RateExchange.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
