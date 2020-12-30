package com.example.fatflat.ui.logged;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ConfigureNotifications extends Application {
    public static final String CHANNEL_1 = "Avisar Telefono Sucio";

    public void onCreate() {
        super.onCreate();

        createChannels();
    }

    private void createChannels(){
        // COMPROVAR QUE LA VERSION ES COMPATIBLE CON ESTE SISTEMA
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel ch1 = new NotificationChannel(
                    CHANNEL_1,
                    "¡Entra en la App y descubre que hay de nuevo!",
                    NotificationManager.IMPORTANCE_HIGH
            );
            ch1.setDescription("Acabas de volver del exterior, recueda que la limpieza es importante");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ch1);
        }
    }
}

