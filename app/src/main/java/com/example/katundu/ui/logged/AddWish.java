package com.example.katundu.ui.logged;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
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

public class AddWish extends AppCompatActivity {

    String[] categorias = new String[8];
    ImageView Atras;
    Button Add_Wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        Atras = findViewById(R.id.AddWish_Atras);
        Add_Wish = findViewById(R.id.ok_button_AddWish);
        final String username = ControladoraPresentacio.getUsername();
        final EditText nameEditText = findViewById(R.id.editTextNom_AddWish);
        final Spinner categoriaSpinner = findViewById(R.id.spinner_AddWish);
        final Switch tipusSwitch = findViewById(R.id.switch_wish);
        final String[] tipus = {"Producte"};
        final EditText paraulesClauEditText = findViewById(R.id.editTextParaulesClau_AddWish);
        final EditText valueEditText = findViewById(R.id.editTextValor_AddW);

        //Inicilizamos las categorias
        categorias[0] = getString(R.string.add_product_category_technology);
        categorias[1] = getString(R.string.add_product_category_home);
        categorias[2] = getString(R.string.add_product_category_beauty);
        categorias[3] = getString(R.string.add_product_category_sports);
        categorias[4] = getString(R.string.add_product_category_fashion);
        categorias[5] = getString(R.string.add_product_category_leisure);
        categorias[6] = getString(R.string.add_product_category_transport);
        categorias[7] = getString(R.string.add_product_category_education);
        /* SPINNER CATEGORIAS */
        //spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias));
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        //Spinner spinner = (Spinner)findViewById(R.id.spinner_AddP);
        categoriaSpinner.setAdapter(adapter);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWish.this, Add.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        Add_Wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean okay = ComprovarCamps(nameEditText, valueEditText, paraulesClauEditText);
                if (okay) {
                    //desactivar atras y subir wish momentaneamente
                    Atras.setEnabled(false);
                    Add_Wish.setEnabled(false);
                    RequestAddWish(categoriaSpinner, tipusSwitch, tipus, username, nameEditText, paraulesClauEditText, valueEditText);
                }
            }
        });
    }

    private boolean ComprovarCamps(EditText nameEditText, EditText valueEditText, EditText paraulesClauEditText) {
        boolean okay = false;
        //Comprovaciones de que ha puesto cosas
        if (nameEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_nombre);
            Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (valueEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_valor);
            Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (paraulesClauEditText.length() == 0) {
            String texterror = getString(R.string.add_product_no_hay_palabras_clave);
            Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (!paraulesClauEditText.getText().toString().contains("#")) {
            String texterror = getString(R.string.add_product_no_hay_hashtag);
            Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
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
                Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return okay;
    }

    private void RequestAddWish(Spinner categoriaSpinner, Switch tipusSwitch, String[] tipus, String username, EditText nameEditText, EditText paraulesClauEditText, EditText valueEditText) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(AddWish.this);

        if(tipusSwitch.isChecked()) tipus[0] = "Servei";
        else tipus[0] = "Producte"; 

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/add-wish?" +
            "user=" + username + "&" +
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
                        if(!response.equals("-1")) { //wish added successfully
                            String wish_added_successfully = getString(R.string.wish_added_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), wish_added_successfully, Toast.LENGTH_SHORT);
                            toast.show();
                            //Volvemos a ListWish
                            Intent intent = new Intent(AddWish.this, ListWish.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(AddWish.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                //reactivar atras y subir wish
                Atras.setEnabled(true);
                Add_Wish.setEnabled(true);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
