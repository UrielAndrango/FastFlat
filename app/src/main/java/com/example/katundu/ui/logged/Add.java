package com.example.katundu.ui.logged;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.katundu.R;
import com.example.katundu.ui.ControladoraAddProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Add extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ControladoraAddProduct.reset();
                    Intent intent = new Intent(Add.this, MenuPrincipal.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    //onNewIntent(intent);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    //finish();
                    break;
                case R.id.navigation_surprise:
                    Intent intent_surprise = new Intent(Add.this, Sorprenme.class);
                    intent_surprise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent_surprise.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent_surprise);
                    overridePendingTransition(0,0);
                    break;
                case R.id.navigation_add:
                    return true;
                case R.id.navigation_xat:
                    Intent intent2 = new Intent(Add.this, ListChat.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    //finish();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Escondemos la Action Bar
        getSupportActionBar().hide();

        //Barra Navegacio en ADD
        BottomNavigationView navView = findViewById(R.id.nav_view_add);
        navView.setSelectedItemId(R.id.navigation_add);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Button add_product = findViewById(R.id.addProduct_add);
        final Button add_wish = findViewById(R.id.addWish_add);

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControladoraAddProduct.reset();
                Intent intent = new Intent(Add.this, AddProduct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        add_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add.this, AddWish.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
    }
}
