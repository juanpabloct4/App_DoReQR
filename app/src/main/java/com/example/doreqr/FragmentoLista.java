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
        // Aquí después conectamos Firebase
        // Por ahora solo muestra el diálogo de éxito
        mostrarDialog("¡Asistencia registrada!",
                "Alumno: " + idAlumno + "\nSe guardó correctamente", true);
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