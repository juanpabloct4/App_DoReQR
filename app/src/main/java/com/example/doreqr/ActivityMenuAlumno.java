package com.example.doreqr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMenuAlumno extends AppCompatActivity {

    public static String usuarioActual;
    public static String idAlumnoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_menu_alumno);

        // RECIBIR USUARIO
        usuarioActual = getIntent().getStringExtra("usuario");

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.navMenu);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemid = item.getItemId();

            if(itemid == R.id.fragQR){

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.frameContainer,
                                new FragmentoGenerarQR()
                        )
                        .commit();
            }

            else if(itemid == R.id.fragPerfil){

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(
                                R.id.frameContainer,
                                new Framento_MiCuenta()
                        )
                        .commit();
            }

            return true;
        });

        // FRAGMENTO INICIAL
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.frameContainer,
                        new FragmentoGenerarQR()
                )
                .commit();
    }
}