package com.example.katundu.ui.logged;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.katundu.R;
import com.example.katundu.ui.ControladoraEditOffer;
import com.example.katundu.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PreviewFotoShow extends AppCompatActivity {

    ImageView preview_foto;
    int pos = ControladoraEditOffer.getNumero_imagen();
    ImageView foto = ControladoraEditOffer.obtener_foto(pos);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto_show);
        foto.setVisibility(View.VISIBLE);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.PreviewFotoShow_Atras);
        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewFotoShow.this, VisualizeOffer.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
        //Imagen que me pasan
        preview_foto= (ImageView)findViewById(R.id.fotoshow);
        BitmapDrawable drawable = (BitmapDrawable) foto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        preview_foto.setImageBitmap(bitmap);
        preview_foto.setVisibility(View.VISIBLE);
    }
}
