package com.example.fatflat.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fatflat.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class RegisterBuyerPreferences extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button registratButton;

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button[] btn_array_BP = new Button[4];
    Button btn_unfocus_BP;
    int[] btn_id_BP = {R.id.btn00, R.id.btn11, R.id.btn22, R.id.btn33};

    Button[] btn_array2_BP = new Button[3];
    Button btn_unfocus2_BP;
    int[] btn_id2_BP = {R.id.btn44, R.id.btn55, R.id.btn66};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_buyer_preferences);
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.RegisterBuyer_Atras);

        final Button SaveButton = findViewById(R.id.RegisterBuyer_SaveButton);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterBuyerPreferences.this, RegisterActivityNEW.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        for(int i = 0; i < btn_array_BP.length; i++){
            btn_array_BP[i] = (Button) findViewById(btn_id_BP[i]);
            btn_array_BP[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn_array_BP[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btn00 :
                            setFocus(btn_unfocus_BP, btn_array_BP[0]);
                            break;

                        case R.id.btn11 :
                            setFocus(btn_unfocus_BP, btn_array_BP[1]);
                            break;

                        case R.id.btn22:
                            setFocus(btn_unfocus_BP, btn_array_BP[2]);
                            break;

                        case R.id.btn33 :
                            setFocus(btn_unfocus_BP, btn_array_BP[3]);
                            break;
                    }
                }
            });
        }
        btn_unfocus_BP = btn_array_BP[0];

        for(int i = 0; i < btn_array2_BP.length; i++){
            btn_array2_BP[i] = (Button) findViewById(btn_id2_BP[i]);
            btn_array2_BP[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn_array2_BP[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btn44 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[0]);
                            break;

                        case R.id.btn55 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[1]);
                            break;

                        case R.id.btn66 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[2]);
                            break;
                    }
                }
            });
        }

        btn_unfocus2_BP = btn_array2_BP[0];

        radioGroup = findViewById(R.id.radioGroup);

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        Spinner spinner3 = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterBuyerPreferences.this, RegisterHouseCharacteristics.class);
                startActivity(intent);
            }
        });
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus_BP = btn_focus;
    }

    private void setFocus2(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus2_BP = btn_focus;
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
        /////////////////////////////////// NEXT PAGE WITHOUT DOING SHIT /////////////////////////////////////
        Intent intent = new Intent(RegisterBuyerPreferences.this, RegisterHouseCharacteristics.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


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
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}