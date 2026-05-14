package com.example.doreqr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class FragmentoGenerarQR extends Fragment {

    private ImageView imageViewQR;
    private Button btnGenerarQR;
    private TextView tvNombreAlumno;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alumno_generarqr, container, false);

        imageViewQR = view.findViewById(R.id.imageViewQR);
        btnGenerarQR = view.findViewById(R.id.btnGenerarQR);
        tvNombreAlumno = view.findViewById(R.id.tvNombreAlumno);

        String idAlumno = getArguments() != null ? getArguments().getString("idAlumno") : "";
        String nombreAlumno = getArguments() != null ? getArguments().getString("nombreAlumno") : "";

        btnGenerarQR.setOnClickListener(v -> {
            generarQR(idAlumno);
            tvNombreAlumno.setText(nombreAlumno);
            tvNombreAlumno.setVisibility(View.VISIBLE);
        });

        return view;
    }

    private void generarQR(String idAlumno) {
        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(idAlumno, BarcodeFormat.QR_CODE, 600, 600);
            imageViewQR.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}