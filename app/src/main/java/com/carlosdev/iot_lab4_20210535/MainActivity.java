package com.carlosdev.iot_lab4_20210535;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.net.ConnectivityManager;
import androidx.core.graphics.Insets;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button botonIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonIngresar = findViewById(R.id.botonIngresar);
        //Se hace toman acciones a partir del test de internet o lo que sea :D
        botonIngresar.setOnClickListener(v -> {
            if (!hayConexion()) {
                mostrarDialogoSinConexion();
            } else {
                Intent intent = new Intent(MainActivity.this, AppActivity.class);
                startActivity(intent);
            }
        });

    }

    //Función que verifca que haya conexión a internet UwU
    private boolean hayConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo redActiva = cm.getActiveNetworkInfo();
            return redActiva != null && redActiva.isConnected();
        }
        return false;
    }

    //En caso no haya conexión esta función muestra el dialog:
    private void mostrarDialogoSinConexion() {
        new AlertDialog.Builder(this)
                .setTitle("Sin conexión a Internet :C")
                .setMessage("No hay conexión a Internet. ¿Deseas abrir la configuración para ver si te quiere?")
                .setPositiveButton("Configuración", (dialog, which) -> {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                })
                .setNegativeButton("Cancelar", null)
                .setCancelable(false)
                .show();
    }
}