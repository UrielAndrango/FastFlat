package com.example.fatflat.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.logged.Mapa;
import com.example.fatflat.ui.logged.MenuPrincipal;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class RegisterHouseCharacteristics extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button registratButton;
    EditText latitudeEditText;
    EditText longitudeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_house_characteristics);
        //getSupportActionBar().hide();

        radioGroup = findViewById(R.id.radioGroup);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final EditText usernameEditText = findViewById(R.id.username_R);
        final EditText nameEditText = findViewById(R.id.nom);
        final EditText passwordEditText = findViewById(R.id.password1);
        final EditText repeatpasswordEditText = findViewById(R.id.password2);
        latitudeEditText = findViewById(R.id.latitud);
        longitudeEditText = findViewById(R.id.longitud);
        final ImageView ubicacio = findViewById(R.id.Ubicacio);

        final EditText descriptionEditText = findViewById(R.id.editTextDescription);
        final EditText birthdateEditText = findViewById(R.id.editTextBirthdate);
        registratButton = findViewById(R.id.SaveButton);

        final EditText questionEditText = findViewById(R.id.editTextQuestion);
        final EditText answerEditText = findViewById(R.id.editTextAnswer);

        registratButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registratButton.setEnabled(false);
                RequestRegister(usernameEditText, passwordEditText, nameEditText, latitudeEditText,
                        longitudeEditText, descriptionEditText, birthdateEditText, questionEditText, answerEditText);
            }
        });
        /*
        ubicacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterBuyerPreferences.this, Mapa.class);
                //startActivity(intent);
                //finish();
                startActivityForResult(intent,2);
            }
        });

         */
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    static boolean valid(String d) {
        if (d.length() != 10) return false;
        String day2s = new StringBuilder().append(d.charAt(0)).append(d.charAt(1)).toString();
        int day = Integer.parseInt(day2s);
        String month2s = new StringBuilder().append(d.charAt(3)).append(d.charAt(4)).toString();
        int month = Integer.parseInt(month2s);
        String year2s = new StringBuilder().append(d.charAt(6)).append(d.charAt(7)).append(d.charAt(8)).append(d.charAt(9)).toString();
        int year = Integer.parseInt(year2s);
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        date.setLenient(false);
        try {
            date.parse(d);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void RequestRegister(final EditText usernameEditText, final EditText passwordEditText,
                                 final EditText nameEditText, final EditText latitudeEditText,
                                 final EditText longitudeEditText, final EditText descriptionEditText, final EditText birthdateEditText,
                                 final EditText questionEditText, final EditText answerEditText) {


        /*

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(RegisterBuyerPreferences.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/user-add?" +
                "un=" + usernameEditText.getText() + "&" +
                "pw=" + passwordEditText.getText() + "&" +
                "n=" + nameEditText.getText() + "&" +
                "lat=" + latitudeEditText.getText() + "&" +
                "lon=" + longitudeEditText.getText();


        if (descriptionEditText.getText().length() > 0)
            url += "&description=" + descriptionEditText.getText();
        if (birthdateEditText.getText().length() > 0)
            url += "&bdate=" + birthdateEditText.getText();

        url += "&q=" + questionEditText.getText() + "&a=" + answerEditText.getText();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //New user added
                            String welcome = getString(R.string.welcome) + usernameEditText.getText().toString();
                            Toast toast = Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_SHORT);
                            toast.show();

                            //Actualizamos Controladora
                            ControladoraPresentacio.setUsername(usernameEditText.getText().toString());
                            ControladoraPresentacio.setPassword(passwordEditText.getText().toString());
                            ControladoraPresentacio.setNom_real(nameEditText.getText().toString());
                            ControladoraPresentacio.setLatitud(latitudeEditText.getText().toString());
                            ControladoraPresentacio.setLongitud(longitudeEditText.getText().toString());
                            ControladoraPresentacio.setDescriptionUser(descriptionEditText.getText().toString());
                            ControladoraPresentacio.setBirthdate(birthdateEditText.getText().toString());
                            ControladoraPresentacio.setQuestion(questionEditText.getText().toString());
                            ControladoraPresentacio.setAnswer(answerEditText.getText().toString());

                            Intent intent = new Intent(RegisterBuyerPreferences.this, MenuPrincipal.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            //finish();
                        }
                        else if(response.equals("1")){ //The username already exists
                            String texterror = getString(R.string.existing_user);
                            Toast toast = Toast.makeText(RegisterBuyerPreferences.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            //reactivar resgistrarse
                            registratButton.setEnabled(true);
                        }
                        else { //response == "-1" Something went wrong
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(RegisterBuyerPreferences.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {  //TODO: aixo ho podem treure?
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(RegisterBuyerPreferences.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                //reactivar resgistrarse
                registratButton.setEnabled(true);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

         */
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}