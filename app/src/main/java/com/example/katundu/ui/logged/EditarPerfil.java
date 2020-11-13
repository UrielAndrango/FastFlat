package com.example.katundu.ui.logged;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditarPerfil extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText latitudeEditText;
    EditText longitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();
        if (mAuth.getCurrentUser() == null) signInAnonymously();

        final ImageView Atras = findViewById(R.id.DeleteAccount_Atras);
        final Button SaveButton = findViewById(R.id.save_button);
        final TextView usernameEditText = findViewById(R.id.TextNomUsuari);
        final EditText nameEditText = findViewById(R.id.editTextNom);
        final EditText passwordEditText = findViewById(R.id.editTextContrasenya);
        latitudeEditText = findViewById(R.id.editTextLatitud);
        longitudeEditText = findViewById(R.id.editTextLongitud);
        final EditText maximumDistanceEditText = findViewById(R.id.editTextMaximumDistance);
        final EditText descriptionEditText = findViewById(R.id.editTextDescription);
        final EditText birthdateEditText = findViewById(R.id.editTextBirthdate);
        final ImageView ubicacio = findViewById(R.id.UbicacioEP);

        //DATOS DEL USUARIO
        usernameEditText.setText(ControladoraPresentacio.getUsername());
        nameEditText.setText(ControladoraPresentacio.getNom_real());
        passwordEditText.setText(ControladoraPresentacio.getPassword());
        latitudeEditText.setText(ControladoraPresentacio.getLatitud());
        longitudeEditText.setText(ControladoraPresentacio.getLongitud());
        maximumDistanceEditText.setText(ControladoraPresentacio.getDistanciaMaxima());
        descriptionEditText.setText(ControladoraPresentacio.getDescriptionUser());
        birthdateEditText.setText(ControladoraPresentacio.getBirthdate());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, Profile.class);
                onNewIntent(intent);
                finish();
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = true;
                if (passwordEditText.getText().length() < 8) {
                    ok = false;
                    String texterror = getString(R.string.invalid_password);
                    Toast toast = Toast.makeText(EditarPerfil.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (birthdateEditText.getText().length() > 0 && !valid(birthdateEditText.getText().toString())) {
                    ok = false;
                    String texterror = getString(R.string.invalid_birthdate);
                    Toast toast = Toast.makeText(EditarPerfil.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (ok) RequestEditarPerfil(usernameEditText, passwordEditText, nameEditText, latitudeEditText, longitudeEditText, maximumDistanceEditText, descriptionEditText, birthdateEditText);
            }
        });

        ubicacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, Mapa.class);
                //startActivity(intent);
                //finish();
                startActivityForResult(intent,2);
            }
        });
    }

    static boolean valid(String d) {
        if (d.length() != 10) return false;
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        date.setLenient(false);
        try {
            date.parse(d);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            private static final String TAG = "SignInStorage" ;
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "signInAnonymously:FAILURE", exception);
            }
        });
    }

    private void RequestEditarPerfil(final TextView usernameEditText, final EditText passwordEditText, final EditText nameEditText,
                                     final EditText latitudeEditText, final EditText longitudeEditText, final EditText maximumDistanceEditText,
                                     final EditText descriptionEditText, final EditText birthdateEditText) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EditarPerfil.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-modify?" +
                "un=" + usernameEditText.getText() + "&" +
                "pw=" + passwordEditText.getText() + "&" +
                "n=" + nameEditText.getText() + "&" +
                "lat=" + latitudeEditText.getText() + "&" +
                "lon=" + longitudeEditText.getText() + "&" +
                "distanciamaxima=" + maximumDistanceEditText.getText();

        url += "&description=" + descriptionEditText.getText() + "&bdate=" + birthdateEditText.getText();
        System.out.println(url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String account_modified_successfully = getString(R.string.account_modified_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), account_modified_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            //Actualizamos Controladora
                            ControladoraPresentacio.setUsername(usernameEditText.getText().toString());
                            ControladoraPresentacio.setPassword(passwordEditText.getText().toString());
                            ControladoraPresentacio.setNom_real(nameEditText.getText().toString());
                            ControladoraPresentacio.setLatitud(latitudeEditText.getText().toString());
                            ControladoraPresentacio.setLongitud(longitudeEditText.getText().toString());
                            ControladoraPresentacio.setDistanciaMaxima(maximumDistanceEditText.getText().toString());
                            ControladoraPresentacio.setDescriptionUser(descriptionEditText.getText().toString());
                            ControladoraPresentacio.setBirthdate(birthdateEditText.getText().toString());

                            //Volvemos a Ajustes
                            Intent intent = new Intent(EditarPerfil.this, Profile.class);
                            onNewIntent(intent);
                            //startActivity(intent);
                            finish();
                        }
                        else { //response == "1" No such user in the database
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(EditarPerfil.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  //TODO: aixo ho podem treure?
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(EditarPerfil.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 2)
        {
            super.onActivityResult(requestCode, resultCode, data);
            latitudeEditText.setText(ControladoraPresentacio.getLatitudMapa());
            longitudeEditText.setText(ControladoraPresentacio.getLongitudMapa());
        }
    }
}

