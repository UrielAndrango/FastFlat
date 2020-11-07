package com.example.katundu.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.katundu.ui.ControladoraAddProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.example.katundu.ui.ControladoraAddProduct;
import com.example.katundu.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().hide();
        final ImageView Atras = findViewById(R.id.AddPost_Atras);
        final Button SubirPost = findViewById(R.id.ok_button_AddPost);
        final EditText title = findViewById(R.id.editTitle_AddPost);
        final EditText descripcion = findViewById(R.id.editDescripcio_AddPost);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPost.this, Forum.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
        SubirPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(AddPost.this);
                boolean okay = false;
                //Comprovaciones de que ha puesto cosas
                if (title.length() == 0) {
                        String texterror = getString(R.string.add_post_no_hay_titulo);
                        Toast toast = Toast.makeText(AddPost.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                } else {
                    if (descripcion.length() == 0) {
                            String texterror = getString(R.string.add_post_no_hay_descripcion);
                            Toast toast = Toast.makeText(AddPost.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    else {
                                    okay = true;
                    }
                }
                if (okay) {

                    Atras.setEnabled(false);

                    String url = "https://us-central1-test-8ea8f.cloudfunctions.net/add-post?" +
                            "user=" + ControladoraPresentacio.getUsername() + "&" +
                            "title=" + title.getText().toString() + "&" +
                            "description=" + descripcion.getText().toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("-1")) { //Error
                                            String texterror = getString(R.string.add_post_general_error);
                                            Toast toast = Toast.makeText(AddPost.this, texterror, Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            String texterror = getString(R.string.add_post_general_ok);
                                            Toast toast = Toast.makeText(AddPost.this, texterror, Toast.LENGTH_SHORT);
                                            toast.show();
                                            Intent intent = new Intent(AddPost.this, Forum.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String texterror = getString(R.string.add_post_general_error);
                                Toast toast = Toast.makeText(AddPost.this, texterror, Toast.LENGTH_SHORT);
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

    }
}
