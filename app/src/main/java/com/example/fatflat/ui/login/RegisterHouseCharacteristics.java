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

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterHouseCharacteristics.this, MenuPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}