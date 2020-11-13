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

public class EditPreference extends AppCompatActivity {

    String[] categorias = new String[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preference);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.EditWish_Atras);
        final Button Modify_Wish = findViewById(R.id.ok_button_EditWish);
        final ImageView DeleteWish = findViewById(R.id.basura_delete_wish);

        final String id = ControladoraPresentacio.getWish_id();
        final EditText nameEditText = findViewById(R.id.editTextNom_EditWish);
        final Spinner categoriaSpinner = findViewById(R.id.spinner_EditWish);
        final Switch tipusSwitch = findViewById(R.id.switch_wish_EW);
        final String[] tipus = {"Producte"};
        final EditText paraulesClauEditText = findViewById(R.id.editTextParaulesClau_EditWish);
        final EditText valueEditText = findViewById(R.id.editTextValor_ModifyW);

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

        //Inicializamos los editText con nuestros datos
        nameEditText.setText(ControladoraPresentacio.getWish_name());
        categoriaSpinner.setSelection(ControladoraPresentacio.getWish_Categoria()); //esto es para cambiar el elemento seleccionado por defecto del spinner
        tipusSwitch.setChecked(ControladoraPresentacio.isWish_Service()); //esto es para cambiar el switch
        paraulesClauEditText.setText(ControladoraPresentacio.getWish_PC());
        valueEditText.setText(ControladoraPresentacio.getWish_Value().toString());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(EditPreference.this, Profile.class);
            onNewIntent(intent);
            //startActivity(intent);
            finish();
            }
        });

        DeleteWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDeleteWish(id);
            }
        });

        Modify_Wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean okay = ComprovarCamps(nameEditText, valueEditText, paraulesClauEditText);
                if (okay) {
                RequestEditWish(categoriaSpinner, tipusSwitch, tipus, id, nameEditText, paraulesClauEditText, valueEditText);
            }
            }
        });
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
