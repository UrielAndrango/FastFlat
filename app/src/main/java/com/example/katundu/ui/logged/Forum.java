package com.example.katundu.ui.logged;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraAddProduct;
import com.example.katundu.ui.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Forum extends AppCompatActivity {
    private TextView mTextMessage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        getSupportActionBar().hide();
        final FloatingActionButton addPost = findViewById(R.id.FAB_addPost);
        final FloatingActionButton deletePosts = findViewById(R.id.Post_Delete);
        final ImageView Atras = findViewById(R.id.Post_Atras);

        refreshLayout = findViewById(R.id.refreshLayout_SP);


        //RequestGetMatches();
        deletePosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forum.this, PersonalPosts.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forum.this, MenuPrincipal.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //recreate();
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                refreshLayout.setRefreshing(false);
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControladoraAddProduct.reset();
                Intent intent = new Intent(Forum.this, AddPost.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        BuscarPosts();
    }
    private void BuscarPosts(){
        final ArrayList<Post> post_list = new ArrayList<>();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(Forum.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-posts";

        // Request a JSONObject response from the provided URL.

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject info = response.getJSONObject(i);

                        String user = info.getString("user");
                        String title = info.getString("title");
                        String description = info.getString("description");
                        String time = info.getString("time");
                        String id= info.getString("id");
                        String time_good = time.substring(0,10) + " | " + time.substring(11,16);

                       Post post = new Post(id,title,user,description, time_good);

                        post_list.add(post);
                    }

                    InicialitzaPosts(post_list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
        /*
         for(int i = 0; i < 10; ++i)
         {
             String user = "2018-02-30 | 20:14         TestUser";
             String title ="Testtitle";
             String description = "TestDescriptionTestDescriptionTestDescriptionTestDescriptionTestDescriptionTestDescriptionTestDescriptionTestDescription";
             Post post = new Post(user,title,description);
             post_list.add(post);
         }
         System.out.println(post_list);

         */
        InicialitzaPosts(post_list);

    }
    private void InicialitzaPosts(ArrayList<Post> post_list) {

        int numBotones = post_list.size();

        //Obtenemos el linear layout donde colocar los botones
        LinearLayout llBotonera = findViewById(R.id.listaPosts);

        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );

        //Creamos los botones en bucle
        for (int i=0; i<numBotones; i++){
            LinearLayout ll = new LinearLayout(Forum.this);
            ll.setOrientation(LinearLayout.VERTICAL);
            TextView text_titol = new TextView(Forum.this);
            TextView text_descripcio = new TextView(Forum.this);
            TextView text_usuari = new TextView(Forum.this);
            //Asignamos propiedades de layout al layout
            ll.setLayoutParams(lp);
            //Asignamos Texto a los textViews
            text_usuari.setText(post_list.get(i).getTime() +"          "+ post_list.get(i).getUser());
            text_titol.setText(post_list.get(i).getTitle());
            text_descripcio.setText(post_list.get(i).getDescription());
            //Le damos el estilo que queremos
            ll.setBackgroundResource(R.drawable.button_rounded);
            text_usuari.setTextColor(Forum.this.getResources().getColor(R.color.colorLetraKatundu));
            text_titol.setTextColor(Forum.this.getResources().getColor(R.color.colorLetraKatundu));
            text_descripcio.setTextColor(Forum.this.getResources().getColor(R.color.colorLetraKatundu));
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            Typeface ITALICTypeface = Typeface.defaultFromStyle(Typeface.ITALIC);
            text_titol.setTypeface(boldTypeface);
            text_usuari.setTypeface(ITALICTypeface);
            text_usuari.setTextSize(12);
            text_titol.setTextSize(17);
            text_descripcio.setTextSize(15);
            //Margenes del layout
            TableRow.LayoutParams paramsll = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            //paramsll.setMargins(left, top, right, bottom);
            if (i==0) paramsll.setMargins(0, 20, 0, 20);
            else paramsll.setMargins(0, 0, 0, 20);
            ll.setLayoutParams(paramsll);
            //Margenes de los textViews

            TableRow.LayoutParams paramsU = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsU.setMargins(25, 20, 25, 10);
            text_usuari.setLayoutParams(paramsU);
            TableRow.LayoutParams paramsRN = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsRN.setMargins(25, 10, 25, 10);
            text_descripcio.setLayoutParams(paramsRN);
            TableRow.LayoutParams paramsV = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            paramsV.setMargins(25, 10, 25, 20);
            text_titol.setLayoutParams(paramsV);

            //Asignamose el Listener al Layout dinamico
            //Añadimos el layout dinamico al layout
            if ( post_list.get(i).getUser().equals("Katundu"))
            {
                ll.setBackgroundResource(R.drawable.drawable_katundu_post);
            }
            ll.addView(text_titol);
            ll.addView(text_descripcio);
            ll.addView(text_usuari);
            llBotonera.addView(ll);

        }
    }
}
