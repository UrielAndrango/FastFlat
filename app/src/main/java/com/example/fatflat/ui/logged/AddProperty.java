package com.example.fatflat.ui.logged;

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
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraAddProduct;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class AddProperty extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String[] categorias = new String[8];
    //StorageReference storageRef = storage.getReference();
    Button Camara;
    ImageView PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4;
    ImageView[] PreviewFotos;
    //HorizontalScrollView scroll_fotos;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    int cantidad_fotos = ControladoraAddProduct.getCantidad_fotos();
    int numero_maximo_fotos = ControladoraAddProduct.getNumero_maximo_fotos();
    ImageView[] fotos = ControladoraAddProduct.getFotos();

    Button foto_gallery;
    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }
        setContentView(R.layout.activity_add_property);

        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.AddProduct_Atras);
        final Button SubirProducto = findViewById(R.id.ok_button_AddP);
        final EditText nombre = findViewById(R.id.editTextNom_AddP);
        final EditText valor = findViewById(R.id.editTextValor_AddP);
        final EditText palabras_clave = findViewById(R.id.editTextParaulesClau_AddP);
        final EditText descripcion = findViewById(R.id.editDescripcio_AddP);
        final Switch switch_type = findViewById(R.id.switch2);

        foto_gallery = findViewById(R.id.galleryButton_AddP);
        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        //SPINNER CATEGORIAS
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_AddP);
        //spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias));
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        //Spinner spinner = (Spinner)findViewById(R.id.spinner_AddP);
        spinner.setAdapter(adapter);

        //Inicilizamos las categorias
        categorias[0] = getString(R.string.add_product_category_technology);
        categorias[1] = getString(R.string.add_product_category_home);
        categorias[2] = getString(R.string.add_product_category_beauty);
        categorias[3] = getString(R.string.add_product_category_sports);
        categorias[4] = getString(R.string.add_product_category_fashion);
        categorias[5] = getString(R.string.add_product_category_leisure);
        categorias[6] = getString(R.string.add_product_category_transport);
        categorias[7] = getString(R.string.add_product_category_education);

        //Inicializamos las fotos
        Camara = findViewById(R.id.camaraButton_AddP);
        PreviewFoto0 = findViewById(R.id.previewFoto_AddP);
        PreviewFoto1 = findViewById(R.id.previewFoto2_AddP);
        PreviewFoto2 = findViewById(R.id.previewFoto3_AddP);
        PreviewFoto3 = findViewById(R.id.previewFoto4_AddP);
        PreviewFoto4 = findViewById(R.id.previewFoto5_AddP);
        PreviewFotos = new ImageView[]{PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4};
        for (int i = 0; i < fotos.length; ++i) {
            PreviewFotos[i].setVisibility(View.INVISIBLE);
        }
        if (cantidad_fotos > 0) {
            for (int i = 0; i < fotos.length; ++i) {
                if (fotos[i] != null) {
                    ImageView imageView = fotos[i];
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    PreviewFotos[i].setImageBitmap(bmp);
                    PreviewFotos[i].setVisibility(View.VISIBLE);

                    //PreviewFotos[i].setVisibility(View.VISIBLE);
                    //PreviewFotos[i].setImageURI(fotos[i]);
                }
            }
        }

        //Inicilizamos todos los atributos que tenia el usuario (si es que tenia)
        nombre.setText(ControladoraAddProduct.getNombre_producto());
        spinner.setSelection(ControladoraAddProduct.getNumero_Categoria());
        valor.setText(ControladoraAddProduct.getValor());
        switch_type.setChecked(ControladoraAddProduct.isEs_servicio());
        palabras_clave.setText(ControladoraAddProduct.getPalabras_clave());
        descripcion.setText(ControladoraAddProduct.getDescripcion());


        //Funcion de los botones
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddProperty.this, MyProperties.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        SubirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(AddProperty.this);
                boolean okay = false;
                //Comprovaciones de que ha puesto cosas
                if (cantidad_fotos == 0) {
                    String texterror = getString(R.string.add_product_no_hay_fotos);
                    Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (nombre.length() == 0) {
                        String texterror = getString(R.string.add_product_no_hay_nombre);
                        Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (valor.length() == 0) {
                            String texterror = getString(R.string.add_product_no_hay_valor);
                            Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            if (palabras_clave.length() == 0) {
                                String texterror = getString(R.string.add_product_no_hay_palabras_clave);
                                Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else if (!palabras_clave.getText().toString().contains("#"))
                            {
                                //TODO: Hay que cambiar esto, 2 opciones (aunque se pueden hacer ambas al mismo tiempo)
                                //Opcion1: Podemos traducir la frase
                                //Opcion2: Poner un text encima de las palabras clave que siempre este ahí
                                String texterror = "Las palabras clave deben empezar con # e ir sin espacios.";
                                Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                if (descripcion.length() == 0) {
                                    String texterror = getString(R.string.add_product_no_hay_descripcion);
                                    Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                                    toast.show();
                                } else {
                                    okay = true;
                                }
                            }
                        }
                    }
                }
                if (okay) {
                    Atras.setEnabled(false);
                    SubirProducto.setEnabled(false);
                    final String[] productid = {""};
                    String type;
                    if (switch_type.isChecked()) type = "Servei";
                    else type = "Producte";

                    String url = "https://us-central1-test-8ea8f.cloudfunctions.net/add-offer?" +
                            "user=" + ControladoraPresentacio.getUsername() + "&" +
                            "name=" + nombre.getText() + "&" +
                            "category=" + spinner.getSelectedItemPosition() + "&" + "type=" + type + "&";
                    String palabras = palabras_clave.getText().toString();
                    //url+="keywords="+palabras+"&";
                    int i = 0;
                    int count = 0;
                    while (i < palabras.length()) {
                        if (palabras.charAt(i) == '#') {
                            ++i;
                            String nueva_palabra = "";
                            while (i < palabras.length() && palabras.charAt(i) != '#') {
                                nueva_palabra += palabras.charAt(i);
                                ++i;
                            }
                            url += "keywords=" + nueva_palabra + "&";
                            if (!nueva_palabra.toString().equals("")) ++count;
                        }
                    }
                    if (count >= 2) {
                        url += "value=" + valor.getText() + "&"
                                + "description=" + descripcion.getText();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("-1")) { //Error
                                            String texterror = getString(R.string.add_product_general_error);
                                            Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                                            toast.show();
                                        } else {
                                            productid[0] = response;
                                            String folder_product = "/products/" + productid[0];
                                            StorageReference imagesRef = storageRef.child("/products").child(folder_product);
                                            for (int k = 0; k < PreviewFotos.length; ++k) {
                                                if (fotos[k] != null) {
                                                    imagesRef = storageRef.child(folder_product).child("product_" + k);
                                                    ImageView imageView = PreviewFotos[k];
                                                    imageView.setDrawingCacheEnabled(true);
                                                    imageView.buildDrawingCache();
                                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                    byte[] data = baos.toByteArray();
                                                    UploadTask uploadTask = imagesRef.putBytes(data);
                                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        }
                                                    });
                                                }
                                            }
                                            //Nos vamos a ListOffer
                                            Intent intent = new Intent(AddProperty.this, MyProperties.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String texterror = getString(R.string.add_product_general_error);
                                Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        // Add the request to the RequestQueue.
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        queue.add(stringRequest);
                    }
                    else
                    {
                        String texterror = getString(R.string.add_product_minimo_dos_keywords);
                        Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                        Atras.setEnabled(true);
                        SubirProducto.setEnabled(true);
                    }
                }
            }
        });

        PreviewFoto0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraAddProduct.setNombre_producto(nombre.getText().toString());
                ControladoraAddProduct.setNumero_Categoria(spinner.getSelectedItemPosition());
                ControladoraAddProduct.setValor(valor.getText().toString());
                ControladoraAddProduct.setEs_servicio(switch_type.isChecked());
                ControladoraAddProduct.setPalabras_clave(palabras_clave.getText().toString());
                ControladoraAddProduct.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraAddProduct.setNumero_imagen(0);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(AddProperty.this, PreviewFoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraAddProduct.setNombre_producto(nombre.getText().toString());
                ControladoraAddProduct.setNumero_Categoria(spinner.getSelectedItemPosition());
                ControladoraAddProduct.setValor(valor.getText().toString());
                ControladoraAddProduct.setEs_servicio(switch_type.isChecked());
                ControladoraAddProduct.setPalabras_clave(palabras_clave.getText().toString());
                ControladoraAddProduct.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraAddProduct.setNumero_imagen(1);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(AddProperty.this, PreviewFoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraAddProduct.setNombre_producto(nombre.getText().toString());
                ControladoraAddProduct.setNumero_Categoria(spinner.getSelectedItemPosition());
                ControladoraAddProduct.setValor(valor.getText().toString());
                ControladoraAddProduct.setEs_servicio(switch_type.isChecked());
                ControladoraAddProduct.setPalabras_clave(palabras_clave.getText().toString());
                ControladoraAddProduct.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraAddProduct.setNumero_imagen(2);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(AddProperty.this, PreviewFoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraAddProduct.setNombre_producto(nombre.getText().toString());
                ControladoraAddProduct.setNumero_Categoria(spinner.getSelectedItemPosition());
                ControladoraAddProduct.setValor(valor.getText().toString());
                ControladoraAddProduct.setEs_servicio(switch_type.isChecked());
                ControladoraAddProduct.setPalabras_clave(palabras_clave.getText().toString());
                ControladoraAddProduct.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraAddProduct.setNumero_imagen(3);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(AddProperty.this, PreviewFoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraAddProduct.setNombre_producto(nombre.getText().toString());
                ControladoraAddProduct.setNumero_Categoria(spinner.getSelectedItemPosition());
                ControladoraAddProduct.setValor(valor.getText().toString());
                ControladoraAddProduct.setEs_servicio(switch_type.isChecked());
                ControladoraAddProduct.setPalabras_clave(palabras_clave.getText().toString());
                ControladoraAddProduct.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraAddProduct.setNumero_imagen(4);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(AddProperty.this, PreviewFoto.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cantidad_fotos < numero_maximo_fotos) {
                    //If system os is >= Marshmallow
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                        PackageManager.PERMISSION_DENIED) {
                            //permission not enable, request it
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            //Show popup to request permissions
                            requestPermissions(permission, PERMISSION_CODE);
                        } else {
                            //permission already granmted
                            openCamera();
                        }
                    } else {
                        //System os < Marshmallow
                        openCamera();
                    }
                }
                else {
                    String texterror = getString(R.string.add_product_muchas_fotos);
                    Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });


    }
    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    private static final String TAG = "SignInStorage" ;

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is callled when user presses Allow or Deny from Permission Request PopUp
        if (cantidad_fotos < numero_maximo_fotos) {
            switch (requestCode) {
                case PERMISSION_CODE: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //permission from popup was garanted
                        openCamera();
                    } else {
                        Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        else {
            String texterror = getString(R.string.add_product_muchas_fotos);
            Toast toast = Toast.makeText(AddProperty.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            //caso de si quiero seleccionar una foto que ya tengo en la galeria
            if (requestCode == PICK_IMAGE) image_uri = data.getData();
            int longitud = ControladoraAddProduct.getFotos().length;
            int i = 0;
            boolean foto_subida_con_exito = false;
            while ((i < longitud) && (foto_subida_con_exito == false)) {
                if (fotos[i] == null) {
                    PreviewFotos[i].setImageURI(image_uri);
                    ImageView imageView = PreviewFotos[i];
                    imageView.setImageURI(image_uri);
                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    PreviewFotos[i].setImageBitmap(bmp);
                    PreviewFotos[i].setVisibility(View.VISIBLE);
                    //Actualizamos la controladora
                    ControladoraAddProduct.add_foto(imageView, i);
                    fotos = ControladoraAddProduct.getFotos();
                    cantidad_fotos = ControladoraAddProduct.getCantidad_fotos();
                    //Salimos del bucle
                    foto_subida_con_exito = true;
                }
                else {
                    //Siguiente hueco de foto
                    ++i;
                }
            }
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
}
