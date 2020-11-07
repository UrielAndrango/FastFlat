package com.example.katundu.ui.logged;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.katundu.R;
import com.example.katundu.ui.ControladoraAddProduct;
import com.example.katundu.ui.ControladoraEditOffer;
import com.example.katundu.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PreviewFotoEdit extends AppCompatActivity {

    ImageView preview_foto;
    int pos = ControladoraEditOffer.getNumero_imagen();
    ImageView foto = ControladoraEditOffer.obtener_foto(pos);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);
        foto.setVisibility(View.VISIBLE);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final Button delete_Button = findViewById(R.id.button);
        final Button OK_Button = findViewById(R.id.button2);

        delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borro la foto en la posicion i de la controladora
                ControladoraEditOffer.borrar_foto(pos);
                ControladoraEditOffer.reordenar_fotos();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference Reference = storageRef.child("/products/"+ ControladoraPresentacio.getOffer_id()).child("product_" + ControladoraEditOffer.getNumero_imagen());
                Reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(PreviewFotoEdit.this, EditOffer.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        String texterror = getString(R.string.preview_fotoE_error);
                        Toast toast = Toast.makeText(PreviewFotoEdit.this, texterror, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                //Vuelvo a la ventan de Add Product
                ControladoraEditOffer.setEstatImatges(true);
                Intent intent = new Intent(PreviewFotoEdit.this, EditOffer.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        OK_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(AddProduct.this, MenuPrincipal.class);
                //startActivity(intent);
                finish();
            }
        });
        //Imagen que me pasan
        preview_foto= (ImageView)findViewById(R.id.imageView);
        BitmapDrawable drawable = (BitmapDrawable) foto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        preview_foto.setImageBitmap(bitmap);
        preview_foto.setVisibility(View.VISIBLE);
    }
}
