package com.example.doreqr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMenuMaestro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_maestro);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navMenuMaestro);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemid = item.getItemId();

            if(itemid == R.id.FragPasarLista){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new FragmentoLista())
                        .commit();
            }
            else if(itemid == R.id.FragConsulta){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new FragmentoConsulta())
                        .commit();
            }
            else if(itemid == R.id.FragHistorial){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameContainer, new FragmentoHistorial())
                        .commit();
            }

            return true;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameContainer, new FragmentoLista())
                .commit();
    }
}