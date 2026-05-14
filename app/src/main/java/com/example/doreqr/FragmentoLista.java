package com.example.doreqr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.activity.result.ActivityResultLauncher;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentoLista extends Fragment {

    private Button btnEscanear;

    // Lanzador del escaner
    private ActivityResultLauncher<ScanOptions> scanLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanLauncher = registerForActivityResult(new ScanContract(), (ScanIntentResult result) -> {
            if (result.getContents() == null) {
                mostrarDialog("Error", "No se pudo escanear el código QR", false);
            } else {
                String idAlumno = result.getContents();
                guardarAsistencia(idAlumno);
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maestro_pasar_lista, container, false);

        btnEscanear = view.findViewById(R.id.btnEscanear);

        btnEscanear.setOnClickListener(v -> abrirEscaner());

        return view;
    }

    private void abrirEscaner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Apunta al código QR del alumno");
        options.setBeepEnabled(true);
        options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(false);
        scanLauncher.launch(options);
    }

    private void guardarAsistencia(String idAlumno) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Primero busca el nombre del alumno
        db.collection("alumnos")
                .document(idAlumno)
                .get()
                .addOnSuccessListener(document -> {

                    if(document.exists()) {

                        String nombreAlumno = document.getString("nombre");

                        // Obtiene fecha y hora actual
                        String fecha = new java.text.SimpleDateFormat(
                                "dd/MM/yyyy", java.util.Locale.getDefault())
                                .format(new java.util.Date());

                        String hora = new java.text.SimpleDateFormat(
                                "HH:mm", java.util.Locale.getDefault())
                                .format(new java.util.Date());

                        // Crea el objeto asistencia
                        java.util.Map<String, Object> asistencia = new java.util.HashMap<>();
                        asistencia.put("idAlumno", idAlumno);
                        asistencia.put("nombreAlumno", nombreAlumno);
                        asistencia.put("fecha", fecha);
                        asistencia.put("hora", hora);

                        // Guarda en Firestore
                        db.collection("asistencias")
                                .add(asistencia)
                                .addOnSuccessListener(ref ->
                                        mostrarDialog("¡Asistencia registrada!",
                                                "Alumno: " + nombreAlumno +
                                                        "\nFecha: " + fecha +
                                                        "\nHora: " + hora, true)
                                )
                                .addOnFailureListener(e ->
                                        mostrarDialog("Error", "No se pudo guardar la asistencia", false)
                                );

                    } else {
                        mostrarDialog("Error", "Alumno no encontrado", false);
                    }
                });
    }

    private void mostrarDialog(String titulo, String mensaje, boolean exito) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(exito ? android.R.drawable.ic_dialog_info
                        : android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Aceptar", null)
                .show();
    }
}