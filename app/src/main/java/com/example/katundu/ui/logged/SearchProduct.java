package com.example.katundu.ui.logged;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
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
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraPresentacio;
import com.example.katundu.ui.Offer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search_products:
                    return true;
                case R.id.navigation_search_users:
                    Intent intent = new Intent(SearchProduct.this, SearchUser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    finish();

                    //Si lo hacemos con ventanas independientes, quitamos los TRUES
                    return true;
                case R.id.navigation_comprador:
                    Intent intentHome = new Intent(SearchProduct.this, MenuPrincipal.class);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentHome);
                    //onNewIntent(intentHome);
                    overridePendingTransition(0,0);
                    finish();
                    //Si lo hacemos con ventanas independientes, quitamos los TRUES
                    return true;
                case R.id.navigation_vendedor:
                    Intent intent_surprise = new Intent(SearchProduct.this, Vendedor.class);
                    intent_surprise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent_surprise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent_surprise);
                    overridePendingTransition(0,0);
                    break;
            }
            return false;
        }
    };

    TextView m;
    LinearLayout ll_products_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();
        //Barra Navegacio Tipus de Cerca
        BottomNavigationView typeSearch = findViewById(R.id.type_search);
        typeSearch.setSelectedItemId(R.id.navigation_search_products);
        typeSearch.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Barra Navegacio Principal
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final ImageView Atras = findViewById(R.id.Search_Atras);
        final SearchView search = findViewById(R.id.search_SP);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchProduct.this, MenuPrincipal.class);
                onNewIntent(intent);
                finish();
            }
        });

        final Spinner categorySpinner = findViewById(R.id.spinner_Category_SP);
        String[] categorias = new String[9];
        categorias[0] = getString(R.string.category_all_SP);
        categorias[1] = getString(R.string.add_product_category_technology);
        categorias[2] = getString(R.string.add_product_category_home);
        categorias[3] = getString(R.string.add_product_category_beauty);
        categorias[4] = getString(R.string.add_product_category_sports);
        categorias[5] = getString(R.string.add_product_category_fashion);
        categorias[6] = getString(R.string.add_product_category_leisure);
        categorias[7] = getString(R.string.add_product_category_transport);
        categorias[8] = getString(R.string.add_product_category_education);
        ArrayAdapter adapter = new ArrayAdapter<String>(SearchProduct.this, android.R.layout.simple_spinner_dropdown_item, categorias);
        categorySpinner.setAdapter(adapter);

        final EditText v = findViewById(R.id.editTextValor_SP);

        final EditText kw = findViewById(R.id.editTextKeywords_SP);

        final Switch switch_type = findViewById(R.id.switch_SP);

        ll_products_found = findViewById(R.id.products_found_SP);

        m = findViewById(R.id.no_products_found_SP);
        m.setVisibility(View.GONE);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Obtenir paràmetres finals per a la request
                //Nom
                final CharSequence nom = search.getQuery();
                //Categoria
                final int cat = categorySpinner.getSelectedItemPosition();
                //Valor
                String value = v.getText().toString();
                //Tipus
                final String type;
                if (switch_type.isChecked()) type = "Servei";
                else type = "Producte";
                //Paraules clau
                String keywords = kw.getText().toString();

                m.setVisibility(View.GONE);
                ll_products_found.setVisibility(View.GONE);
                RequestSearchProducts(nom, cat, value, type, keywords);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void RequestSearchProducts(CharSequence nom, int cat, String valor, String tipus, String keywords) {
        RequestQueue queue = Volley.newRequestQueue(SearchProduct.this);
        String req = "https://us-central1-test-8ea8f.cloudfunctions.net/get-products?" + "username="
                + ControladoraPresentacio.getUsername() + "&name=" + nom;

        if (cat != 0) req += "&category=" + (cat-1);
        if (!valor.equals("")) req += "&value=" + valor;
        req += "&type=" + tipus;
        for (int i = 0; i < keywords.length(); ++i) {
            if (keywords.charAt(i) == '#') {
                ++i;
                String new_keyword = "";
                while (i < keywords.length() && keywords.charAt(i) != '#') {
                    new_keyword += keywords.charAt(i);
                    ++i;
                }
                --i; //tornem al # de la seguent paraula per a la seguent iteracio
                req += "&keyword=" + new_keyword;
            }
        }

        // Request a JSON Array response from the provided URL.
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.GET, req, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<Offer> products_list = new ArrayList<>();
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject product = response.getJSONObject(i);
                        String user = product.getString("user");
                        String id = product.getString("id");
                        String name = product.getString("name");
                        String category = product.getString("category");
                        String type = product.getString("type");
                        JSONArray keywords_array = product.getJSONArray("keywords");
                        String value = product.getString("value");
                        String description = product.getString("description");
                        String keywords = "";
                        for (int j = 0; j < keywords_array.length(); ++j) {
                            String keyword = keywords_array.getString(j);
                            keywords += "#" + keyword;
                        }

                        Offer offer = new Offer(id,name,Integer.parseInt(category),type,keywords,Integer.parseInt(value),description,user);
                        if (!products_list.contains(offer)) products_list.add(offer);
                    }

                    ll_products_found.setVisibility(View.VISIBLE);

                    if (products_list.size() == 0) {
                        m.setVisibility(View.VISIBLE);
                        ll_products_found.setVisibility(View.GONE);
                    }

                    else InitializeProductsButton(products_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                    String notProductsFound = getString(R.string.search_products_bad_search);
                    Toast toast = Toast.makeText(getApplicationContext(), notProductsFound, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
            }
        });

        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsArrayRequest);
    }

    private void InitializeProductsButton(ArrayList<Offer> products_list) {
        ControladoraPresentacio.setOffer_List(products_list);
        int nProducts = products_list.size();

        ll_products_found = findViewById(R.id.products_found_SP);
        ll_products_found.removeAllViews();

        LinearLayout pareja = new LinearLayout(SearchProduct.this);
        boolean show_product = true;
        boolean odd_nProducts = false;
        if (nProducts % 2 == 1) odd_nProducts = true;

        for (int i = 0; i < nProducts; ++i) {
            if (show_product && i % 2 == 0) {
                pareja = new LinearLayout(SearchProduct.this);
                pareja.setOrientation(LinearLayout.HORIZONTAL);
                TableRow.LayoutParams paramsP = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                if (i==0) paramsP.setMargins(0, 20, 0, 20);
                else paramsP.setMargins(0, 0, 0, 20);
                pareja.setLayoutParams(paramsP);
            }
            //Definimos el layout y lo que contiene (foto+precio+nombre)
            LinearLayout ll = new LinearLayout(SearchProduct.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            final ImageView foto = new ImageView(SearchProduct.this);
            TextView preu_producte = new TextView(SearchProduct.this);
            TextView nom_producte = new TextView(SearchProduct.this);
            //Asignamos Texto a los textViews
            preu_producte.setText(products_list.get(i).getValue() + "€");
            nom_producte.setText(products_list.get(i).getName() + "");
            //Le damos el estilo que queremos
            ll.setBackgroundResource(R.drawable.button_rounded);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference Reference = storageRef.child("/products/" + products_list.get(i).getId()).child("product_0");
            Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    foto.setImageBitmap(bmp);
                    foto.setVisibility(View.VISIBLE);
                }
            });
            preu_producte.setTextColor(SearchProduct.this.getResources().getColor(R.color.colorLetraKatundu));
            nom_producte.setTextColor(SearchProduct.this.getResources().getColor(R.color.colorLetraKatundu));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            preu_producte.setTypeface(boldTypeface);
            nom_producte.setTypeface(boldTypeface);
            preu_producte.setTextSize(18);
            nom_producte.setTextSize(18);
            //Margenes del layout
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsll.weight = 1;
            paramsll.height = 800;
            if (i%2==0) paramsll.setMargins(0, 0, 10, 0);
            else paramsll.setMargins(10, 0, 0, 0);
            ll.setLayoutParams(paramsll);
            //Margenes de los textViews
            TableRow.LayoutParams paramsFoto = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsFoto.weight = 1;
            paramsFoto.setMargins(25, 25, 25, 10);
            foto.setLayoutParams(paramsFoto);
            TableRow.LayoutParams paramsPrecio = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsPrecio.setMargins(25, 10, 25, 10);
            preu_producte.setLayoutParams(paramsPrecio);
            TableRow.LayoutParams paramsN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsN.setMargins(25, 10, 25, 20);
            nom_producte.setLayoutParams(paramsN);
            //Le escondemos el nombre del producto en la descripcion
            ll.setContentDescription(products_list.get(i).getName());
            //Asignamose el Listener al Layout dinamico
            ll.setOnClickListener(new SearchProduct.LayoutOnClickListener(SearchProduct.this));
            //Añadimos el layout dinamico al layout
            ll.addView(foto);
            ll.addView(preu_producte);
            ll.addView(nom_producte);
            if (!show_product) ll.setVisibility(View.INVISIBLE);
            pareja.addView(ll);
            if (show_product && i % 2 == 0) ll_products_found.addView(pareja);

            if (odd_nProducts && i == nProducts - 1) {
                --i;
                show_product = false;
                odd_nProducts = false;
            }
        }
    }

    private class LayoutOnClickListener implements View.OnClickListener {
        public LayoutOnClickListener(SearchProduct searchProduct) {
        }

        @Override
        public void onClick(View view) {
            //Provando que funciona el layout en modo boton
            Toast.makeText(getApplicationContext(), view.getContentDescription().toString(), Toast.LENGTH_SHORT).show();

            //Pasamos los datos del deseo a la controladora
            Offer info_offer = ControladoraPresentacio.getOffer_perName(view.getContentDescription().toString());
            //Pasamos los datos del deseo a la controladora
            ControladoraPresentacio.setOffer_id(info_offer.getId());
            ControladoraPresentacio.setOffer_name(info_offer.getName());
            ControladoraPresentacio.setOffer_Categoria(info_offer.getCategory());
            boolean type = true;
            String tipus = info_offer.getType();
            if (tipus.equals("Product")) type = false;
            ControladoraPresentacio.setOffer_Service(type);
            ControladoraPresentacio.setOffer_PC(info_offer.getKeywords());
            ControladoraPresentacio.setOffer_Value(info_offer.getValue());
            ControladoraPresentacio.setOffer_Description(info_offer.getDescription());
            //Nos vamos a la ventana de EditOffer
            Intent intent = new Intent(SearchProduct.this, VisualizeOffer.class);
            startActivity(intent);
        }
    }
}














