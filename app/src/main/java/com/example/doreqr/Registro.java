package com.example.doreqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
    TextView titulo;
    TextInputEditText txtNombre, txtUsuario, txtPassword,
            txtTelefono, txtFecha;
    Spinner spinnerInstrumento;
    RadioGroup radioGroup;
    FirebaseFirestore db;
    boolean modoEditar = false;
    String idAlumnoEditar = "";

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
        titulo = findViewById(R.id.tvTitulo);

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

        if(getIntent().hasExtra("modoEditar")) {
            modoEditar = true;
            btnRegistarRegistro.setText("Guardar Cambios");
            titulo.setText("Actualizar");
            cargarDatosEditar();
        }

        btnRegistarRegistro.setOnClickListener(v -> {
            if(modoEditar) {
                actualizarAlumno();
            } else {
                registrarAlumno();
            }
        });
    }

    private void registrarAlumno() {
        if(!validarCampos()) {
            return;
        }

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
                .whereEqualTo("usuario", usuario)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if(!queryDocumentSnapshots.isEmpty()) {
                        txtUsuario.setError(
                                "Ese usuario ya existe"
                        );
                        txtUsuario.requestFocus();
                    } else {
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

                                    finish();
                                });
                    }
                });
    }

    private void cargarDatosEditar() {
        idAlumnoEditar =
                getIntent().getStringExtra("idAlumno");

        db.collection("alumnos")
                .document(idAlumnoEditar)
                .get()
                .addOnSuccessListener(doc -> {
                    if(doc.exists()) {
                        txtNombre.setText(
                                doc.getString("nombre")
                        );
                        txtUsuario.setText(
                                doc.getString("usuario")
                        );
                        txtPassword.setText(
                                doc.getString("password")
                        );
                        txtTelefono.setText(
                                doc.getString("telefono")
                        );
                        txtFecha.setText(
                                doc.getString("fechaNacimiento")
                        );
                        // SPINNER
                        String instrumento = doc.getString("instrumento");
                        ArrayAdapter adapter = (ArrayAdapter) spinnerInstrumento.getAdapter();
                        int posicion = adapter.getPosition(instrumento);
                        spinnerInstrumento.setSelection(posicion);

                        // RADIO BUTTON
                        String sexo =
                                doc.getString("sexo");
                        if(sexo.equals("Hombre")) {
                            radioGroup.check(R.id.rb_hombre);
                        } else {
                            radioGroup.check(R.id.rb_mujer);
                        }
                    }
                });
    }

    private void actualizarAlumno() {
        if(!validarCampos()) {
            return;
        }

        String nombre = txtNombre.getText().toString();
        String usuario = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String fecha = txtFecha.getText().toString();
        String instrumento =
                spinnerInstrumento
                        .getSelectedItem()
                        .toString();
        int idSeleccionado = radioGroup.getCheckedRadioButtonId();
        RadioButton radioSeleccionado = findViewById(idSeleccionado);
        String sexo = radioSeleccionado.getText().toString();

        db.collection("alumnos")
                .document(idAlumnoEditar)
                .update(
                        "nombre", nombre,
                        "usuario", usuario,
                        "password", password,
                        "telefono", telefono,
                        "fechaNacimiento", fecha,
                        "sexo", sexo,
                        "instrumento", instrumento
                )
                .addOnSuccessListener(unused -> {
                    Toast.makeText(
                            this,
                            "Datos actualizados",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                });
    }

    private boolean tieneSecuencia(String pass) {
        pass = pass.toLowerCase();

        for(int i = 0; i < pass.length() - 2; i++) {
            int c1 = pass.charAt(i);
            int c2 = pass.charAt(i + 1);
            int c3 = pass.charAt(i + 2);

            // abc o 123
            if(c2 == c1 + 1 && c3 == c2 + 1) {
                return true;
            }

            // cba o 321
            if(c2 == c1 - 1 && c3 == c2 - 1) {
                return true;
            }
        }

        return false;
    }

    private String validarPassword(String pass) {

        if(pass.length() < 8) {
            return "Mínimo 8 caracteres";
        }

        if(!pass.matches(".*[A-Z].*")) {
            return "Debe contener una mayúscula";
        }

        if(!pass.matches(".*[a-z].*")) {
            return "Debe contener una minúscula";
        }

        if(!pass.matches(".*[0-9].*")) {
            return "Debe contener un número";
        }

        if(!pass.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Debe contener un carácter especial";
        }

        if(tieneSecuencia(pass)) {
            return "No se permiten secuencias";
        }

        return "";
    }

    private String validarNombre(String nombre) {
        if(nombre.trim().isEmpty()) {
            return "El nombre no puede estar vacío";
        }

        if(!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+")) {
            return "Solo letras y espacios";
        }

        return "";
    }

    private String validarTelefono(String telefono) {
        if(!telefono.matches("[0-9]{10}")) {
            return "Debe tener 10 dígitos";
        }
        return "";
    }

    private String validarUsuario(String usuario) {
        if(usuario.trim().isEmpty()) {
            return "Ingresa un usuario";
        }

        if(usuario.length() < 4) {
            return "Mínimo 4 caracteres";
        }

        return "";
    }
    private boolean validarCampos() {
        String errorNombre = validarNombre(txtNombre.getText().toString());

        if(!errorNombre.isEmpty()) {
            txtNombre.setError(errorNombre);
            txtNombre.requestFocus();
            return false;
        }

        String errorUsuario = validarUsuario(txtUsuario.getText().toString());

        if(!errorUsuario.isEmpty()) {
            txtUsuario.setError(errorUsuario);
            txtUsuario.requestFocus();
            return false;
        }

        String errorPassword = validarPassword(txtPassword.getText().toString());

        if(!errorPassword.isEmpty()) {
            txtPassword.setError(errorPassword);
            txtPassword.requestFocus();
            return false;
        }

        String errorTelefono = validarTelefono(txtTelefono.getText().toString());

        if(!errorTelefono.isEmpty()) {
            txtTelefono.setError(errorTelefono);
            txtTelefono.requestFocus();
            return false;
        }

        if(txtFecha.getText().toString().trim().isEmpty()) {
            txtFecha.setError("Selecciona una fecha");
            return false;
        }

        if(radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(
                    this,
                    "Selecciona un sexo",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        return true;
    }
}