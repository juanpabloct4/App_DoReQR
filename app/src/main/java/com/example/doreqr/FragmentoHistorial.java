package com.example.doreqr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FragmentoHistorial extends Fragment {

    RecyclerView recyclerHistorial;

    FirebaseFirestore db;

    List<Asistencia> listaAsistencias;

    AsistenciaAdapter adapter;

    public FragmentoHistorial() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(
                R.layout.fragment_maestro_historial,
                container,
                false
        );

        db = FirebaseFirestore.getInstance();

        recyclerHistorial =
                view.findViewById(R.id.recyclerHistorial);

        recyclerHistorial.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        listaAsistencias = new ArrayList<>();

        adapter = new AsistenciaAdapter(
                getContext(),
                listaAsistencias
        );

        recyclerHistorial.setAdapter(adapter);

        cargarAsistencias();

        return view;
    }

    private void cargarAsistencias() {

        db.collection("asistencias")
                .get()

                .addOnSuccessListener(queryDocumentSnapshots -> {

                    listaAsistencias.clear();

                    for(var doc :
                            queryDocumentSnapshots.getDocuments()) {

                        Asistencia asistencia =
                                doc.toObject(Asistencia.class);

                        listaAsistencias.add(asistencia);
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}