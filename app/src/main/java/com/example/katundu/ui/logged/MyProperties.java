package com.example.katundu.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.katundu.R;
import com.example.katundu.ui.ControladoraAddProduct;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyProperties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.MyPropierties_Atras);
        final FloatingActionButton addProperty = findViewById(R.id.FAB_addProperty_MyP);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProperties.this, Profile.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControladoraAddProduct.reset();
                Intent intent = new Intent(MyProperties.this, AddProperty.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}