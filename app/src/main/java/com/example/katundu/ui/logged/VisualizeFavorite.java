package com.example.katundu.ui.logged;

        import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.example.katundu.R;
import com.example.katundu.ui.ControladoraEditOffer;
import com.example.katundu.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class VisualizeFavorite extends AppCompatActivity {

    String[] categorias = new String[7];
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    final int[] numImages = {0};
    Uri image_uri;
    ImageView PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4;
    ImageView[] PreviewFotos;
    final Integer[] cantidad_fotos = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String folder_product = "/products/Q9KGzX0vB7rC5aakqBEp/";
        final StorageReference imagesRef = storageRef.child(folder_product);
        setContentView(R.layout.activity_visualize_favorite);
        final ImageView DeleteFavorite = findViewById(R.id.basura_delete_offer);
        final ImageView Atras = findViewById(R.id.VisualizeFavorite_Atras);

        final String id = ControladoraPresentacio.getfavorite_id();
        final TextView nameFavorite = findViewById(R.id.TextNom_VisualizeFavorite);
        final TextView categoriaFavorite  = findViewById(R.id.spinner_VisualizeFavorite);
        final Switch tipusFavorite  = findViewById(R.id.switch3);
        final TextView paraulesClauFavorite  = findViewById(R.id.ParaulesClau_VisualizeFavorite);
        final TextView valueFavorite  = findViewById(R.id.TextValor_VisualizeFavorite);
        final TextView descriptionFavorite  = findViewById(R.id.textDescripcion_VisualizeFavorite);

        // FOTOS
        PreviewFoto0 = findViewById(R.id.previewFoto_VisualizeFavorite);
        PreviewFoto1 = findViewById(R.id.previewFoto2_VisualizeFavorite);
        PreviewFoto2 = findViewById(R.id.previewFoto3_VisualizeFavorite);
        PreviewFoto3 = findViewById(R.id.previewFoto4_VisualizeFavorite);
        PreviewFoto4 = findViewById(R.id.previewFoto5_VisualizeFavorite);
        PreviewFotos = new ImageView[]{PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4};

        for (int i = 0; i < 5; ++i) {
            final int finalI = i;
            StorageReference Reference2 = storageRef.child("/products/" + ControladoraPresentacio.getfavorite_id()).child("product_" + i);
            System.out.println(Reference2.toString());
            Reference2.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    cantidad_fotos[0] +=1;
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
                    PreviewFotos[finalI].setImageBitmap(bmp);
                    PreviewFotos[finalI].setVisibility(View.VISIBLE);
                }
            });
        }
        ControladoraEditOffer.setFotos(PreviewFotos);

        PreviewFoto0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ControladoraEditOffer.add_foto(PreviewFotos[0],0);
                ControladoraEditOffer.setNumero_imagen(0);
                Intent intent = new Intent(VisualizeFavorite.this, PreviewFotoShow.class);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[1],1);
                ControladoraEditOffer.setNumero_imagen(1);
                Intent intent = new Intent(VisualizeFavorite.this,  PreviewFotoShow.class);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[2],2);
                ControladoraEditOffer.setNumero_imagen(2);
                Intent intent = new Intent(VisualizeFavorite.this,  PreviewFotoShow.class);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[3],3);
                ControladoraEditOffer.setNumero_imagen(3);
                Intent intent = new Intent(VisualizeFavorite.this,  PreviewFotoShow.class);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[4],4);
                ControladoraEditOffer.setNumero_imagen(4);
                Intent intent = new Intent(VisualizeFavorite.this,  PreviewFotoShow.class);
                startActivity(intent);
                //finish();
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


        //Inicializamos los editText con nuestros datos
        nameFavorite .setText(ControladoraPresentacio.getFavorite_name());
        categoriaFavorite .setText(categorias[ControladoraPresentacio.getWish_Categoria()]); //esto es para cambiar el elemento seleccionado por defecto del spinner
        tipusFavorite .setChecked(ControladoraPresentacio.isfavorite_Service()); //esto es para cambiar el switch
        tipusFavorite .setClickable(false);
        paraulesClauFavorite .setText(ControladoraPresentacio.getfavorite_PC());
        valueFavorite .setText(ControladoraPresentacio.getfavorite_Value().toString());
        descriptionFavorite .setText(ControladoraPresentacio.getfavorite_Description());

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeFavorite.this, ListFavorites.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
        DeleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDeleteFavorite(id);
            }
        });
    }
    private void RequestDeleteFavorite(final String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(VisualizeFavorite.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/delete-favorite?" + "un=" + ControladoraPresentacio.getUsername() + "&id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String offer_deleted_successfully = getString(R.string.favorite_removed_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), offer_deleted_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            Intent intent = new Intent(VisualizeFavorite.this, ListFavorites.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else { //response == "1" No s'ha esborrat el desig
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(VisualizeFavorite.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(VisualizeFavorite.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            //int longitud = fotos.length;
            PreviewFotos[cantidad_fotos[0]].setImageURI(image_uri);
            PreviewFotos[cantidad_fotos[0]].setImageURI(image_uri);
            cantidad_fotos[0]++;
        }
    }
}
