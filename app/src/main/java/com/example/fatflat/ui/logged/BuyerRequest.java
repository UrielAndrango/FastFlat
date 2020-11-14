package com.example.fatflat.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BuyerRequest extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_request);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.BuyerRequest_Atras);
        final ImageView FotoPerfil = findViewById(R.id.BuyerRequest_FotoPerfil);
        final TextView Username = findViewById(R.id.BR_username_dinamico);
        final TextView Name = findViewById(R.id.BR_name_dinamico);
        final TextView Accuracy = findViewById(R.id.BR_accuracy_dinamico);


        //Inicializamos los TextView con nuestros datos
        Username.setText(ControladoraPresentacio.getOffer_name());
        Name.setText(ControladoraPresentacio.getOffer_name());
        Accuracy.setText(ControladoraPresentacio.getOffer_Value().toString());


        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyerRequest.this, ListBuyersRequests.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        StorageReference Reference = storageRef.child("/products/" + ControladoraPresentacio.getOffer_id()).child("product_0");
        Reference.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Redondeamos las esquinas de las fotos
                //bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2*8);
                FotoPerfil.setImageBitmap(bmp);
                FotoPerfil.setVisibility(View.VISIBLE);
            }
        });


    }
}