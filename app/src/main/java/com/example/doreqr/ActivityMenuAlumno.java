package com.example.doreqr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMenuAlumno extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_alumno);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navMenu);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemid = item.getItemId();

            if(itemid == R.id.fragQR){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new FragmentoGenerarQR())
                        .commit();
            }
            else if(itemid == R.id.fragPerfil){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new Framento_MiCuenta())
                        .commit();
            }
            return true;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, new FragmentoGenerarQR())
                .commit();
    }
}