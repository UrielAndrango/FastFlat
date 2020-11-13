package com.example.katundu.ui.logged;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katundu.R;
import com.example.katundu.ui.ControladoraAddProduct;
import com.example.katundu.ui.ControladoraPresentacio;

public class PreviewFoto extends AppCompatActivity {

    ImageView preview_foto;
    int pos = ControladoraAddProduct.getNumero_imagen();
    ImageView foto = ControladoraAddProduct.obtener_foto(pos);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_foto);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final Button delete_Button = findViewById(R.id.button);
        final Button OK_Button = findViewById(R.id.button2);

        delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Borro la foto en la posicion i de la controladora
                ControladoraAddProduct.borrar_foto(pos);
                ControladoraAddProduct.reordenar_fotos();
                //Vuelvo a la ventan de Add Product
                Intent intent = new Intent(PreviewFoto.this, AddProperty.class);
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
        preview_foto = (ImageView)findViewById(R.id.imageView);
        ImageView imageView = foto;
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64*2);
        preview_foto.setImageBitmap(bmp);
    }
}
