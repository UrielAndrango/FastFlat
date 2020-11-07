package com.example.katundu.ui.logged;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraPosts;
import com.example.katundu.ui.ControladoraPresentacio;

public class VisualizePosts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_posts);
        getSupportActionBar().hide();
        final ImageView Atras = findViewById(R.id.AddPost_Atras);
        final ImageView eliminar = findViewById(R.id.basura_delete_post);
        final Button SubirPost = findViewById(R.id.ok_button_AddPost);
        final EditText title = findViewById(R.id.editTitle_AddPost);
        final EditText descripcion = findViewById(R.id.editDescripcio_AddPost);
        title.setText(ControladoraPosts.gettitle());
        descripcion.setText(ControladoraPosts.getDescription());
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizePosts.this, PersonalPosts.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
        SubirPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(VisualizePosts.this);
                boolean okay = false;
                //Comprovaciones de que ha puesto cosas
                if (title.length() == 0) {
                    String texterror = getString(R.string.add_post_no_hay_titulo);
                    Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (descripcion.length() == 0) {
                        String texterror = getString(R.string.add_post_no_hay_descripcion);
                        Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        okay = true;
                    }
                }
                if (okay) {

                    Atras.setEnabled(false);

                    String url = "https://us-central1-test-8ea8f.cloudfunctions.net/modify-post?" +
                            "user=" + ControladoraPresentacio.getUsername() + "&" +
                            "title=" + title.getText().toString() + "&" +
                            "description=" + descripcion.getText().toString()+ "&id=" + ControladoraPosts.getid();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("-1")) { //Error
                                        String texterror = getString(R.string.post_modified_successfully);
                                        Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                                        toast.show();
                                    } else {
                                        String texterror = getString(R.string.add_post_general_ok);
                                        Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                                        toast.show();
                                        Intent intent = new Intent(VisualizePosts.this, Forum.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String texterror = getString(R.string.add_post_general_error);
                            Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                    // Add the request to the RequestQueue.
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);

                }
            }
        });
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mContext = getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(VisualizePosts.this, R.style.myDialog));
                builder.setCancelable(true);
                builder.setTitle(R.string.eliminar_post);
                builder.setMessage(R.string.eliminar_post_confirmacion);
                builder.setPositiveButton(R.string.confirmar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Atras.setEnabled(false);
                                RequestQueue queue = Volley.newRequestQueue(VisualizePosts.this);
                                String url = "https://us-central1-test-8ea8f.cloudfunctions.net/delete-post?" +
                                        "id=" + ControladoraPosts.getid();

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("-1")) { //Error
                                                    String texterror = getString(R.string.add_post_general_error);
                                                    Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                } else {
                                                    String texterror = getString(R.string.post_deleted_successfully);
                                                    Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                                                    toast.show();
                                                    Intent intent = new Intent(VisualizePosts.this, PersonalPosts.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String texterror = getString(R.string.add_post_general_error);
                                        Toast toast = Toast.makeText(VisualizePosts.this, texterror, Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });

                                // Add the request to the RequestQueue.
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                queue.add(stringRequest);

                            }
                        });
                builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}
