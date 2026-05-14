package com.example.doreqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentoConsulta extends Fragment {

    RecyclerView recyclerAlumnos;

    Spinner spinnerSexo, spinnerInstrumento;

    FirebaseFirestore db;

    List<Alumno> listaAlumnos;

    List<Alumno> listaOriginal;

    AlumnoAdapter adapter;

    TextView tvTotalAlumnos;

    public FragmentoConsulta() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_maestro_consulta,
                container,
                false
        );

        db = FirebaseFirestore.getInstance();

        recyclerAlumnos =
                view.findViewById(R.id.recyclerAlumnos);

        spinnerSexo =
                view.findViewById(R.id.spinnerSexo);

        spinnerInstrumento =
                view.findViewById(R.id.spinnerInstrumento);

        tvTotalAlumnos =
                view.findViewById(R.id.tvTotalAlumnos);

        // RecyclerView
        recyclerAlumnos.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaAlumnos = new ArrayList<>();

        listaOriginal = new ArrayList<>();

        adapter = new AlumnoAdapter(
                getContext(),
                listaAlumnos
        );

        recyclerAlumnos.setAdapter(adapter);

        cargarSpinners();

        cargarAlumnos();

        // LISTENER SEXO
        spinnerSexo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        filtrarAlumnos();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                });

        // LISTENER INSTRUMENTO
        spinnerInstrumento.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        filtrarAlumnos();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                });

        return view;
    }

    private void cargarSpinners() {

        // SEXO
        String[] sexos = {
                "Todos",
                "Hombre",
                "Mujer"
        };

        ArrayAdapter<String> adapterSexo =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        sexos
                );

        adapterSexo.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerSexo.setAdapter(adapterSexo);

        // INSTRUMENTOS
        String[] instrumentos = {
                "Todos",
                "Guitarra",
                "Piano",
                "Batería",
                "Violín"
        };

        ArrayAdapter<String> adapterInstrumento =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        instrumentos
                );

        adapterInstrumento.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerInstrumento.setAdapter(
                adapterInstrumento
        );
    }

    private void cargarAlumnos() {

        db.collection("alumnos")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listaAlumnos.clear();

                    listaOriginal.clear();

                    for(var doc :
                            queryDocumentSnapshots.getDocuments()) {

                        Alumno alumno =
                                doc.toObject(Alumno.class);

                        listaAlumnos.add(alumno);

                        listaOriginal.add(alumno);
                    }

                    adapter.notifyDataSetChanged();

                    tvTotalAlumnos.setText(
                            "Total de alumnos: "
                                    + listaAlumnos.size()
                    );
                });
    }

    private void filtrarAlumnos() {

        String sexoSeleccionado =
                spinnerSexo.getSelectedItem().toString();

        String instrumentoSeleccionado =
                spinnerInstrumento.getSelectedItem().toString();

        listaAlumnos.clear();

        for(Alumno alumno : listaOriginal) {

            boolean coincideSexo =
                    sexoSeleccionado.equals("Todos")
                            ||
                            alumno.getSexo()
                                    .equals(sexoSeleccionado);

            boolean coincideInstrumento =
                    instrumentoSeleccionado.equals("Todos")
                            ||
                            alumno.getInstrumento()
                                    .equals(instrumentoSeleccionado);

            if(coincideSexo && coincideInstrumento) {

                listaAlumnos.add(alumno);
            }
        }

        adapter.notifyDataSetChanged();
        tvTotalAlumnos.setText(
                "Total de alumnos: "
                        + listaAlumnos.size()
        );
    }
}