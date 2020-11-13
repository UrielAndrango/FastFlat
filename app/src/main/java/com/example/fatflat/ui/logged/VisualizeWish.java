package com.example.fatflat.ui.logged;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraPresentacio;

public class VisualizeWish extends AppCompatActivity {

    String[] categorias = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_wish);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.ViewWish_Atras);

        final String idWish = ControladoraPresentacio.getWish_id();
        final TextView nomWishTextView = findViewById(R.id.viewWish);
        final TextView nomWishEditText = findViewById(R.id.editTextNom_ViewWish);
        final TextView categoriaWish = findViewById(R.id.categoria_ViewWish);
        final Switch tipusWish = findViewById(R.id.switch_wish_VW);
        final TextView paraulesClauWish = findViewById(R.id.editTextParaulesClau_ViewWish);
        final TextView valueWish = findViewById(R.id.editTextValor_ViewW);

        //Inicilizamos las categorias
        categorias[0] = getString(R.string.add_product_category_technology);
        categorias[1] = getString(R.string.add_product_category_home);
        categorias[2] = getString(R.string.add_product_category_beauty);
        categorias[3] = getString(R.string.add_product_category_sports);
        categorias[4] = getString(R.string.add_product_category_fashion);
        categorias[5] = getString(R.string.add_product_category_leisure);
        categorias[6] = getString(R.string.add_product_category_transport);


        //Inicializamos los editText con nuestros datos
        nomWishTextView.setText(ControladoraPresentacio.getWish_name());
        nomWishEditText.setText(ControladoraPresentacio.getWish_name());
        categoriaWish.setText(categorias[ControladoraPresentacio.getWish_Categoria()]); //esto es para cambiar el elemento seleccionado por defecto del spinner
        tipusWish.setChecked(ControladoraPresentacio.isWish_Service()); //esto es para cambiar el switch
        tipusWish.setClickable(false);
        paraulesClauWish.setText(ControladoraPresentacio.getWish_PC());
        valueWish.setText(ControladoraPresentacio.getWish_Value().toString());


        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeWish.this, VisualizeListWUser.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });
    }
}
