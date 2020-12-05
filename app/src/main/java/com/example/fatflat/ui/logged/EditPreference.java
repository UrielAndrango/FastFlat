package com.example.fatflat.ui.logged;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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

public class EditPreference extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    Button[] btn_array_BP = new Button[4];
    Button btn_unfocus_BP;
    int[] btn_id_BP = {R.id.EditPreference_btn00, R.id.EditPreference_btn11, R.id.EditPreference_btn22, R.id.EditPreference_btn33};

    Button[] btn_array2_BP = new Button[3];
    Button btn_unfocus2_BP;
    int[] btn_id2_BP = {R.id.EditPreference_btn44, R.id.EditPreference_btn55, R.id.EditPreference_btn66};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preference);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.EditWish_Atras);
        final Button Modify_Wish = findViewById(R.id.ok_button_EditPreference);
        final ImageView DeleteWish = findViewById(R.id.basura_delete_wish);
        final TextView Subtitle = findViewById(R.id.EditPreference_subtitle);
        final TextView Accuracy = findViewById(R.id.ModificarPreferencias_Accuracy);
        final SeekBar seekBar = findViewById(R.id.seekBar);

        //Justificacion texto
        //Subtitle.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        //Definir Accurracy por defecto
        int Accuracy_default = 90;
        Accuracy.setText("" + Accuracy_default + "%");
        seekBar.setProgress(Accuracy_default);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(EditPreference.this, Profile.class);
            onNewIntent(intent);
            //startActivity(intent);
            finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Accuracy.setText("" + i + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
/*
        DeleteWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDeleteWish(id);
            }
        });
*/

        for(int i = 0; i < btn_array_BP.length; i++){
            btn_array_BP[i] = (Button) findViewById(btn_id_BP[i]);
            btn_array_BP[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn_array_BP[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.EditPreference_btn00 :
                            setFocus(btn_unfocus_BP, btn_array_BP[0]);
                            break;

                        case R.id.EditPreference_btn11 :
                            setFocus(btn_unfocus_BP, btn_array_BP[1]);
                            break;

                        case R.id.EditPreference_btn22:
                            setFocus(btn_unfocus_BP, btn_array_BP[2]);
                            break;

                        case R.id.EditPreference_btn33 :
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
                        case R.id.EditPreference_btn44 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[0]);
                            break;

                        case R.id.EditPreference_btn55 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[1]);
                            break;

                        case R.id.EditPreference_btn66 :
                            setFocus2(btn_unfocus2_BP, btn_array2_BP[2]);
                            break;
                    }
                }
            });
        }

        btn_unfocus2_BP = btn_array2_BP[0];

        radioGroup = findViewById(R.id.EditPreference_radioGroup);

        Spinner spinner = findViewById(R.id.EditPreference_spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        Spinner spinner2 = findViewById(R.id.EditPreference_spinner3);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        //spinner2.setOnItemSelectedListener(this);

        Spinner spinner3 = findViewById(R.id.EditPreference_spinner4);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        //spinner3.setOnItemSelectedListener(this);

        Modify_Wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPreference.this, MenuPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
                /*
                boolean okay = ComprovarCamps(nameEditText, valueEditText, paraulesClauEditText);
                if (okay) {
                RequestEditWish(categoriaSpinner, tipusSwitch, tipus, id, nameEditText, paraulesClauEditText, valueEditText);
            }*/
            }
        });
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

    private boolean ComprovarCamps(EditText nameEditText, EditText valueEditText, EditText paraulesClauEditText) {
        boolean okay = false;
        //Comprovaciones de que ha puesto cosas
        if (nameEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_nombre);
            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (valueEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_valor);
            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (paraulesClauEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_palabras_clave);
            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (!paraulesClauEditText.getText().toString().contains("#")) {
            String texterror = getString(R.string.add_product_no_hay_hashtag);
            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(paraulesClauEditText.getText().toString().contains("#")) {
            String palabras = paraulesClauEditText.getText().toString();
            int i = 0;
            int count = 0;
            while( i < palabras.length()) {
                if (palabras.charAt(i) == '#') {
                    ++i;
                    StringBuilder nueva_palabra = new StringBuilder();
                    while (i < palabras.length() && palabras.charAt(i) != '#' ) {
                        nueva_palabra.append(palabras.charAt(i));
                        ++i;
                    }
                    if(!nueva_palabra.toString().equals("")) ++count;
                }
            }
            if(count >= 2) okay = true;
            else {
                String texterror = getString(R.string.add_product_minimo_dos_keywords);
                Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return okay;
    }

    private void RequestDeleteWish(final String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EditPreference.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/delete-wish?" + "id=" + id;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String wish_deleted_successfully = getString(R.string.wish_deleted_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), wish_deleted_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            //Volvemos a User
                            Intent intent = new Intent(EditPreference.this, MyProperties.class);
                            startActivity(intent);
                            finish();
                        }
                        else { //response == "1" No s'ha esborrat el desig
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void RequestEditWish(Spinner categoriaSpinner, Switch tipusSwitch, String[] tipus, String id, EditText nameEditText, EditText paraulesClauEditText, EditText valueEditText) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EditPreference.this);

        if(tipusSwitch.isChecked()) tipus[0] = "Servei";
        else tipus[0] = "Producte";

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/modify-wish?" +
            "id=" + id + "&" +
            "name=" + nameEditText.getText() + "&" +
            "category=" + categoriaSpinner.getSelectedItemPosition() + "&" +
            "type=" + tipus[0] + "&";

            String palabras = paraulesClauEditText.getText().toString();
            int i = 0;
            while( i < palabras.length()) {
                if (palabras.charAt(i) == '#') {
                    ++i;
                    String nueva_palabra = "";
                    while (i < palabras.length() && palabras.charAt(i) != '#' ) {
                        nueva_palabra += palabras.charAt(i);
                        ++i;
                    }
                    url += "keywords=" + nueva_palabra + "&";
                }
            }
        url+="value="+valueEditText.getText();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String wish_modified_successfully = getString(R.string.wish_modified_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), wish_modified_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            //Volvemos a User
                            Intent intent = new Intent(EditPreference.this, ListOffer.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(response.equals("3")) { //Account modified successfully
                            String wish_empty_values = getString(R.string.empty_values);
                            Toast toast = Toast.makeText(getApplicationContext(), wish_empty_values, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else { //response == "1" No such user in the database
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(EditPreference.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
