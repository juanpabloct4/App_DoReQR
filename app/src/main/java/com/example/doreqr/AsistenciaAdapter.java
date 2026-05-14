package com.example.doreqr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AsistenciaAdapter
        extends RecyclerView.Adapter<AsistenciaAdapter.ViewHolder> {

    Context context;
    List<Asistencia> listaAsistencias;

    public AsistenciaAdapter(
            Context context,
            List<Asistencia> listaAsistencias
    ) {

        this.context = context;
        this.listaAsistencias = listaAsistencias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_asistencia,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        Asistencia asistencia =
                listaAsistencias.get(position);

        holder.tvNombre.setText(
                asistencia.getNombreAlumno()
        );

        holder.tvInfo.setText(
                asistencia.getFecha()
                        + " • "
                        + asistencia.getHora()
        );
    }

    @Override
    public int getItemCount() {
        return listaAsistencias.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNombre, tvInfo;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvNombre =
                    itemView.findViewById(
                            R.id.tvNombreAsistencia
                    );

            tvInfo =
                    itemView.findViewById(
                            R.id.tvInfoAsistencia
                    );
        }
    }
}