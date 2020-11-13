package com.example.katundu.ui.logged;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.katundu.ui.ControladoraEditOffer;
import com.example.katundu.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditProperty extends AppCompatActivity {

    String[] categorias = new String[8];
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    final int[] numImages = {0};
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    Button Camara;
    Button foto_gallery;
    private static final int PICK_IMAGE = 100;
    ImageView PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4;
    ImageView PreviewFoto0_final, PreviewFoto1_final, PreviewFoto2_final, PreviewFoto3_final, PreviewFoto4_final;
    ImageView[] PreviewFotos;
    ImageView[] PreviewFotos_final;
    final Integer[] cantidad_fotos = {0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String folder_product = "/products/Q9KGzX0vB7rC5aakqBEp/";
        final StorageReference imagesRef = storageRef.child(folder_product);
        setContentView(R.layout.activity_edit_propierty);
        final ImageView Atras = findViewById(R.id.EditOffer_Atras);
        final Button Modify_Offer = findViewById(R.id.ok_button_EditOffer);

        final String id = ControladoraPresentacio.getOffer_id();
        final EditText nameEditText = findViewById(R.id.editTextNom_EditOffer);
        final Spinner categoriaSpinner = findViewById(R.id.spinner_EditOffer);
        final Switch tipusSwitch = findViewById(R.id.switch3);
        final String[] tipus = {"Producte"};
        final EditText paraulesClauEditText = findViewById(R.id.editTextParaulesClau_EditOffer);
        final EditText valueEditText = findViewById(R.id.editTextValor_EditOffer);
        final EditText descriptionEditText = (TextInputEditText)findViewById(R.id.editDescripcio_EditOffer);
        final ImageView DeleteWish = findViewById(R.id.basura_delete_offer);
        foto_gallery = findViewById(R.id.galleryButton_EditOffer);
        foto_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        // FOTOS

        Camara = findViewById(R.id.camaraButton_EditOffer);
        PreviewFoto0 = findViewById(R.id.previewFoto_EditOffer);
        PreviewFoto1 = findViewById(R.id.previewFoto2_EditOffer);
        PreviewFoto2 = findViewById(R.id.previewFoto3_EditOffer);
        PreviewFoto3 = findViewById(R.id.previewFoto4_EditOffer);
        PreviewFoto4 = findViewById(R.id.previewFoto5_EditOffer);
        PreviewFotos = new ImageView[]{PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4};
        PreviewFotos_final = new ImageView[]{PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4};
        final int[] cantidad = {0};
        ControladoraEditOffer.setCantidad_fotos(0);
        for (int i = 0; i < 5; ++i) {
            final int finalI = i;
            final StorageReference Reference2 = storageRef.child("/products/" + ControladoraPresentacio.getOffer_id()).child("product_" + i);
            Reference2.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            cantidad[0] += 1;
                            System.out.println("quantitattt" + cantidad[0]);
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            //Redondeamos las esquinas de las fotos
                            bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                            PreviewFotos[finalI].setImageBitmap(bmp);
                            PreviewFotos[finalI].setVisibility(View.VISIBLE);
                            ControladoraEditOffer.setCantidad_fotos(cantidad[0]);
                            ControladoraEditOffer.setFotos(PreviewFotos);
                        }
            });
        }



        PreviewFoto0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraPresentacio.setOffer_name(nameEditText.getText().toString());
                ControladoraPresentacio.setOffer_Categoria(categoriaSpinner.getSelectedItemPosition());
                ControladoraPresentacio.setOffer_Value((Integer.valueOf(valueEditText.getText().toString())));
                ControladoraPresentacio.setOffer_Service(tipusSwitch.isChecked());
                ControladoraPresentacio.setOffer_PC(paraulesClauEditText.getText().toString());
                ControladoraEditOffer.add_foto(PreviewFotos[0],0);
                //ControladoraEditOffer.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraEditOffer.setNumero_imagen(0);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(EditProperty.this, PreviewFotoEdit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraPresentacio.setOffer_name(nameEditText.getText().toString());
                ControladoraPresentacio.setOffer_Categoria(categoriaSpinner.getSelectedItemPosition());
                ControladoraPresentacio.setOffer_Value((Integer.valueOf(valueEditText.getText().toString())));
                ControladoraPresentacio.setOffer_Service(tipusSwitch.isChecked());
                ControladoraPresentacio.setOffer_PC(paraulesClauEditText.getText().toString());
                ControladoraEditOffer.add_foto(PreviewFotos[1],1);
                //ControladoraEditOffer.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraEditOffer.setNumero_imagen(1);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(EditProperty.this, PreviewFotoEdit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraPresentacio.setOffer_name(nameEditText.getText().toString());
                ControladoraPresentacio.setOffer_Categoria(categoriaSpinner.getSelectedItemPosition());
                ControladoraPresentacio.setOffer_Value((Integer.valueOf(valueEditText.getText().toString())));
                ControladoraPresentacio.setOffer_Service(tipusSwitch.isChecked());
                ControladoraPresentacio.setOffer_PC(paraulesClauEditText.getText().toString());
                ControladoraEditOffer.add_foto(PreviewFotos[2],2);
                //ControladoraEditOffer.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraEditOffer.setNumero_imagen(2);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(EditProperty.this, PreviewFotoEdit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraPresentacio.setOffer_name(nameEditText.getText().toString());
                ControladoraPresentacio.setOffer_Categoria(categoriaSpinner.getSelectedItemPosition());
                ControladoraPresentacio.setOffer_Value((Integer.valueOf(valueEditText.getText().toString())));
                ControladoraPresentacio.setOffer_Service(tipusSwitch.isChecked());
                ControladoraPresentacio.setOffer_PC(paraulesClauEditText.getText().toString());
                ControladoraEditOffer.add_foto(PreviewFotos[3],3);
                //ControladoraEditOffer.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraEditOffer.setNumero_imagen(3);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(EditProperty.this, PreviewFotoEdit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar datos
                ControladoraPresentacio.setOffer_name(nameEditText.getText().toString());
                ControladoraPresentacio.setOffer_Categoria(categoriaSpinner.getSelectedItemPosition());
                ControladoraPresentacio.setOffer_Value((Integer.valueOf(valueEditText.getText().toString())));
                ControladoraPresentacio.setOffer_Service(tipusSwitch.isChecked());
                ControladoraPresentacio.setOffer_PC(paraulesClauEditText.getText().toString());
                ControladoraEditOffer.add_foto(PreviewFotos[4],4);
                //ControladoraEditOffer.setDescripcion(descripcion.getText().toString());
                //Indicamos la foto a la controladora que querremos ver
                ControladoraEditOffer.setNumero_imagen(4);
                //Nos vamos a la ventana de Preview
                Intent intent = new Intent(EditProperty.this, PreviewFotoEdit.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ControladoraEditOffer.getCantidad_fotos() < 5) {
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
                    Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        DeleteWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDeleteOffer(id);
            }
        });
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();


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
        nameEditText.setText(ControladoraPresentacio.getOffer_name());
        categoriaSpinner.setSelection(ControladoraPresentacio.getOffer_Categoria()); //esto es para cambiar el elemento seleccionado por defecto del spinner
        tipusSwitch.setChecked(ControladoraPresentacio.isOffer_Service()); //esto es para cambiar el switch
        paraulesClauEditText.setText(ControladoraPresentacio.getOffer_PC());
        valueEditText.setText(ControladoraPresentacio.getOffer_Value().toString());
        descriptionEditText.setText(ControladoraPresentacio.getOffer_Description());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ControladoraEditOffer.getEstatImatges())
                {
                    String texterror = getString(R.string.modified_images);
                    Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Intent intent = new Intent(EditProperty.this, ListOffer.class);
                    onNewIntent(intent);
                    //startActivity(intent);
                    finish();
                }
            }
        });

        Modify_Offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean okay = false;
                final int[] cantitat = {0};
                //Comprovaciones de que ha puesto cosas
                if ( cantitat[0] == 0 && ControladoraEditOffer.getCantidad_fotos() == 0)
                {
                    String texterror = getString(R.string.add_product_no_hay_fotos);
                    Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if (nameEditText.length() == 0) {
                    String texterror = getString(R.string.add_product_no_hay_nombre);
                    Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    if (paraulesClauEditText.length() == 0) {
                        String texterror = getString(R.string.add_product_no_hay_palabras_clave);
                        Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if (!paraulesClauEditText.getText().toString().contains("#"))
                    {
                        //TODO: Hay que cambiar esto, 2 opciones (aunque se pueden hacer ambas al mismo tiempo)
                        //Opcion1: Podemos traducir la frase
                        //Opcion2: Poner un text encima de las palabras clave que siempre este ahí
                        String texterror = "Las palabras clave deben empezar con # e ir sin espacios.";
                        Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        if (descriptionEditText.length() == 0) {
                            String texterror = getString(R.string.add_product_no_hay_descripcion);
                            Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            okay = true;
                        }
                    }
                }
                if (okay) {
                    ControladoraEditOffer.setEstatImatges(false);
                    Atras.setEnabled(false);
                    Modify_Offer.setEnabled(false);
                    RequestEditOffer(categoriaSpinner, tipusSwitch, tipus, id, nameEditText, paraulesClauEditText, valueEditText,descriptionEditText,Atras,Modify_Offer);
                }
            }
        });
    }
    private void RequestDeleteOffer(final String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(EditProperty.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/delete-offer?" + "id=" + id;
        for (int i = 0; i < 5; ++i) {
            final int finalI = i;
            StorageReference Reference2 = storageRef.child("/products/" + ControladoraPresentacio.getOffer_id()).child("product_" + i);
            Reference2.delete();
        }
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String offer_deleted_successfully = getString(R.string.offer_deleted_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), offer_deleted_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            //Volvemos a User
                            Intent intent = new Intent(EditProperty.this, ListOffer.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else { //response == "1" No s'ha esborrat el desig
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
        ControladoraEditOffer.setCantidad_fotos(ControladoraEditOffer.getCantidad_fotos()+1);
    }
    private void RequestEditOffer(Spinner categoriaSpinner, Switch tipusSwitch, String[] tipus, String id, EditText nameEditText, EditText paraulesClauEditText, EditText valueEditText, EditText descriptionEditText, final ImageView Atras, final Button Modify_Offer ) {
        // Instantiate the RequestQueue.
        for (int i = 0; i < 5; ++i) {
            final int finalI = i;
            StorageReference Reference2 = storageRef.child("/products/" + ControladoraPresentacio.getOffer_id()).child("product_" + i);
            Reference2.delete();
        }
        String folder_product = "/products/" + id;
        StorageReference imagesRef = storageRef.child("/products").child(folder_product);
        int nombre_fotos = 0;
        for (int k = 0; k < 5;++k)
        {
            ImageView imageView = PreviewFotos[k];
            if(imageView.getDrawable() != null) {
                imagesRef = storageRef.child(folder_product).child("product_"+nombre_fotos);
                ++nombre_fotos;
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
        RequestQueue queue = Volley.newRequestQueue(EditProperty.this);

        if(tipusSwitch.isChecked()) tipus[0] = "Servei";
        else tipus[0] = "Producte";

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/modify-offer?" +
                "id=" + id + "&" +
                "name=" + nameEditText.getText().toString() + "&" +
                "category=" + categoriaSpinner.getSelectedItemPosition() + "&" +
                "type=" + tipus[0] + "&" +
                "value=" + valueEditText.getText()+"&"+
                "description= " +  descriptionEditText.getText() + "&";
        String palabras = paraulesClauEditText.getText().toString();
        //url+="keywords="+palabras+"&";
        int i = 0;
        int count = 0;
        while( i < palabras.length()) {
            if (palabras.charAt(i) == '#') {
                ++i;
                String nueva_palabra = "";
                while (i < palabras.length() && palabras.charAt(i) != '#' ) {
                    nueva_palabra += palabras.charAt(i);
                    ++i;
                }
                url += "keywords=" + nueva_palabra + "&";
                if(!nueva_palabra.toString().equals("")) ++count;
            }
        }
        if(count >= 2)
        {
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("0")) { //Account modified successfully
                                String offer_modified_successfully = getString(R.string.offer_modified_successfully);
                                Toast toast = Toast.makeText(getApplicationContext(), offer_modified_successfully, Toast.LENGTH_SHORT);
                                toast.show();

                                //Volvemos a User
                                Intent intent = new Intent(EditProperty.this, ListOffer.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else if(response.equals("3")) { //Account modified successfully
                                String offer_empty_values = getString(R.string.empty_values);
                                Toast toast = Toast.makeText(getApplicationContext(), offer_empty_values, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else { //response == "1" No such user in the database
                                String texterror = getString(R.string.error);
                                Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String texterror = getString(R.string.error);
                    Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
                    toast.show();
                    Atras.setEnabled(true);
                    Modify_Offer.setEnabled(true);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
        else {
            String texterror = getString(R.string.add_product_minimo_dos_keywords);
            Toast toast = Toast.makeText(EditProperty.this, texterror, Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            //int longitud = fotos.length;
            if (requestCode == PICK_IMAGE) image_uri = data.getData();
            if (ControladoraEditOffer.getCantidad_fotos() <=4) {
                PreviewFotos[ControladoraEditOffer.getCantidad_fotos()].setImageURI(image_uri);
                ControladoraEditOffer.setCantidad_fotos(ControladoraEditOffer.getCantidad_fotos() + 1);
            }
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
}
