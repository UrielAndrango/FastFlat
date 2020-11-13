package com.example.katundu.ui.logged;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katundu.R;

import java.util.Locale;

public class EditarIdioma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_idioma);
        //Escondemos la Action Bar porque usamos la ToolBar
        getSupportActionBar().hide();

        final ImageView Atras = findViewById(R.id.EditarIdioma_Atras);
        final Button Guardar_idioma = findViewById(R.id.save_idioma);
        final RadioGroup idiomasDisponibles = findViewById(R.id.idiomas_disponibles);
        final RadioButton espanol = findViewById(R.id.idioma_esp);
        final RadioButton catalan = findViewById(R.id.idioma_cat);
        final RadioButton ingles = findViewById(R.id.idioma_eng);

        //Inicializamos con el idioma del usuario en este momento

        switch (loadLocale()) {
        //switch (Locale.getDefault().getLanguage()) {
            case "es":
                espanol.setChecked(true);
                break;
            case "ca":
                catalan.setChecked(true);
                break;
            case "en":
                ingles.setChecked(true);
                break;
        }

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarIdioma.this, Profile.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        Guardar_idioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ahora si hacemos el cambio
                if (idiomasDisponibles.getCheckedRadioButtonId() == espanol.getId()) {
                    setLocale("es");
                    recreate();
                }
                else if (idiomasDisponibles.getCheckedRadioButtonId() == catalan.getId()) {
                    setLocale("ca");
                    recreate();
                }
               else if (idiomasDisponibles.getCheckedRadioButtonId() == ingles.getId()) {
                    setLocale("en");
                    recreate();
                }
                //"Reiniciamos" y vamos a menuPrincipal
                Intent intent = new Intent(EditarIdioma.this, MenuPrincipal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void setLocale(String idioma) {
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", idioma);
        editor.apply();
    }

    public String loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String idioma = prefs.getString("My_Lang", "");
        setLocale(idioma);

        return idioma;
    }
}
