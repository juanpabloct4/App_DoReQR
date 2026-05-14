package com.example.doreqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Framento_MiCuenta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Framento_MiCuenta extends Fragment {

    // VARIABLES FIREBASE Y COMPONENTES
    TextView tvNombre, tvUsuario, tvTelefono,
            tvFecha, tvInstrumento, tvGenero;

    Button btnEditar;

    FirebaseFirestore db;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Framento_MiCuenta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alumno_micuenta.
     */
    // TODO: Rename and change types and number of parameters
    public static Framento_MiCuenta newInstance(String param1, String param2) {
        Framento_MiCuenta fragment = new Framento_MiCuenta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_alumno_micuenta,
                container,
                false
        );

        db = FirebaseFirestore.getInstance();

        tvNombre = view.findViewById(R.id.tvNombre);
        tvUsuario = view.findViewById(R.id.tvUsuario);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvFecha = view.findViewById(R.id.tvFecha);
        tvInstrumento = view.findViewById(R.id.tvInstrumento);
        tvGenero = view.findViewById(R.id.tvGenero);

        btnEditar = view.findViewById(R.id.btnEditar);

        cargarDatosAlumno();

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    getContext(),
                    Registro.class
            );

            intent.putExtra("modoEditar", true);

            intent.putExtra(
                    "idAlumno",
                    ActivityMenuAlumno.idAlumnoActual
            );

            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        cargarDatosAlumno();
    }

    private void cargarDatosAlumno() {

        String idAlumno =
                ActivityMenuAlumno.idAlumnoActual;

        db.collection("alumnos")
                .document(idAlumno)
                .get()

                .addOnSuccessListener(doc -> {

                    if(doc.exists()) {

                        tvNombre.setText(
                                doc.getString("nombre")
                        );

                        tvUsuario.setText(
                                doc.getString("usuario")
                        );

                        tvTelefono.setText(
                                doc.getString("telefono")
                        );

                        tvFecha.setText(
                                doc.getString("fechaNacimiento")
                        );

                        tvInstrumento.setText(
                                doc.getString("instrumento")
                        );

                        tvGenero.setText(
                                doc.getString("sexo")
                        );
                    }
                });
    }
}