package com.example.doreqr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    Button btnRegistrarLogin;
    Button btnIngresar;

    TextInputEditText txtUsuario, txtPassword;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        btnRegistrarLogin = findViewById(R.id.btnRegistrar);
        btnIngresar = findViewById(R.id.btnIngresar);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);

        btnRegistrarLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    Registro.class
            );

            startActivity(intent);

        });

        btnIngresar.setOnClickListener(v -> validarUsuario());
    }

    private void validarUsuario() {
        String usuario = txtUsuario.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        // LOGIN MAESTRO
        if(usuario.equals("maestro") && password.equals("maestro")) {
            Toast.makeText(
                    this,
                    "Bienvenido Maestro",
                    Toast.LENGTH_SHORT
            ).show();
            Intent intent = new Intent(
                    MainActivity.this,
                    ActivityMenuMaestro.class
            );
            startActivity(intent);
            finish();
            return;
        }

        // VALIDACIÓN NORMAL
        if(usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        db.collection("alumnos")
                .whereEqualTo("usuario", usuario)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        boolean existe = false;

                        // DECLARAR VARIABLES AQUÍ
                        String idAlumno = "";
                        String nombreAlumno = "";

                        for(QueryDocumentSnapshot document
                                : task.getResult()) {

                            existe = true;

                            idAlumno =
                                    document.getId();

                            nombreAlumno =
                                    document.getString("nombre");

                            ActivityMenuAlumno.usuarioActual =
                                    document.getString("usuario");

                            ActivityMenuAlumno.idAlumnoActual =
                                    document.getId();

                            break;
                        }

                        if(existe) {
                            Toast.makeText(
                                    this,
                                    "Bienvenido",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    ActivityMenuAlumno.class
                            );
                            intent.putExtra("usuario", usuario);

                            // Pasa el ID y nombre al siguiente activity
                            intent.putExtra("idAlumno", idAlumno);
                            intent.putExtra("nombreAlumno", nombreAlumno);

                            startActivity(intent);

                            finish();
                        }
                        if(!existe) {
                            Toast.makeText(
                                    this,
                                    "Usuario o contraseña incorrectos",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else {
                        Toast.makeText(
                                this,
                                "Error al consultar",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}