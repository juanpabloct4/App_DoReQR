package com.example.doreqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.DatePickerDialog;
import java.util.Calendar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro extends AppCompatActivity {

    Button btnRegistarRegistro;

    TextInputEditText txtNombre, txtUsuario, txtPassword,
            txtTelefono, txtFecha;

    Spinner spinnerInstrumento;

    RadioGroup radioGroup;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        txtNombre = findViewById(R.id.name);
        txtUsuario = findViewById(R.id.user);
        txtPassword = findViewById(R.id.Pass);
        txtTelefono = findViewById(R.id.phone);
        txtFecha = findViewById(R.id.birth);
        txtFecha.setOnClickListener(v -> {

            Calendar calendario = Calendar.getInstance();

            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    Registro.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String fecha = selectedDay + "/"
                                + (selectedMonth + 1) + "/"
                                + selectedYear;

                        txtFecha.setText(fecha);

                    },
                    year,
                    month,
                    day
            );

            dialog.show();
        });

        spinnerInstrumento = findViewById(R.id.instrumento);

        radioGroup = findViewById(R.id.radioGroupSexo);

        btnRegistarRegistro = findViewById(R.id.btn1);

        // Spinner
        String[] instrumentos = {
                "Guitarra",
                "Piano",
                "Batería",
                "Violín"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                instrumentos
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerInstrumento.setAdapter(adapter);

        btnRegistarRegistro.setOnClickListener(v -> registrarAlumno());
    }

    private void registrarAlumno() {

        String nombre = txtNombre.getText().toString();
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String fecha = txtFecha.getText().toString();

        String instrumento = spinnerInstrumento.getSelectedItem().toString();

        int idSeleccionado = radioGroup.getCheckedRadioButtonId();

        RadioButton radioSeleccionado = findViewById(idSeleccionado);

        String sexo = radioSeleccionado.getText().toString();

        Alumno nuevoAlumno = new Alumno(
                nombre,
                usuario,
                password,
                telefono,
                fecha,
                sexo,
                instrumento
        );

        db.collection("alumnos")
                .add(nuevoAlumno)
                .addOnSuccessListener(documentReference -> {

                    Toast.makeText(
                            this,
                            "Alumno registrado",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            this,
                            MainActivity.class
                    );

                    startActivity(intent);

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Error al registrar",
                            Toast.LENGTH_SHORT
                    ).show();

                });
    }
}