package com.example.katundu.ui.logged;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.login.LoginActivity;

public class DeleteAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        //Escondemos la Action Bar porque usamos la ToolBar, aunque se podria usar la Action Bar
        getSupportActionBar().hide();
        final ImageView Atras = findViewById(R.id.DeleteAccount_Atras);
        //modificar estos botones, esto es solo provisional
        final String username = ControladoraPresentacio.getUsername();
        final Button deleteAccount = findViewById(R.id.deleteaccount);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeleteAccount.this, Profile.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDeleteAccount(username);
            }
        });
    }

    private void RequestDeleteAccount(String username) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(DeleteAccount.this);

        System.out.println(username);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-delete?" +
                "un=" + username;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        if(response.equals("0")) { //Account deleted successfully
                            String account_deleted_successfully = getString(R.string.account_deleted_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), account_deleted_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            Intent intent = new Intent(DeleteAccount.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                        }
                        else { //response == "1" Something went wrong
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(DeleteAccount.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  //TODO: aixo ho podem treure?
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(DeleteAccount.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
