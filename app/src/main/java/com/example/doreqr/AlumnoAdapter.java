package com.example.doreqr;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AlumnoAdapter
        extends RecyclerView.Adapter<AlumnoAdapter.ViewHolder> {

    Context context;

    List<Alumno> listaAlumnos;

    public AlumnoAdapter(Context context,
                         List<Alumno> listaAlumnos) {

        this.context = context;
        this.listaAlumnos = listaAlumnos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_alumno,
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

        Alumno alumno = listaAlumnos.get(position);

        holder.tvNombre.setText(alumno.getNombre());

        int edad = calcularEdad(
                alumno.getFechaNacimiento()
        );

        holder.tvInstrumento.setText(
                "Instrumento: "
                        + alumno.getInstrumento()
                        + " • "
                        + edad
                        + " años"
        );

        holder.itemView.setOnClickListener(v -> {

            String info =
                    "Usuario: " + alumno.getUsuario() +
                            "\n\nTeléfono: " + alumno.getTelefono() +
                            "\n\nFecha de Nacimiento: " + alumno.getFechaNacimiento() +
                            "\n\nGénero: " + alumno.getSexo() +
                            "\n\nInstrumento: " + alumno.getInstrumento();

            new AlertDialog.Builder(context)
                    .setTitle(alumno.getNombre())
                    .setMessage(info)
                    .setPositiveButton("Cerrar", null)
                    .show();
        });

        holder.itemView.setOnLongClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Eliminar alumno")
                    .setMessage(
                            "¿Deseas eliminar a "
                                    + alumno.getNombre()
                                    + "?"
                    )

                    .setPositiveButton("Eliminar",
                            (dialog, which) -> {

                                eliminarAlumno(alumno);
                            })

                    .setNegativeButton("Cancelar", null)

                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listaAlumnos.size();
    }

    private void eliminarAlumno(Alumno alumno) {

        FirebaseFirestore db =
                FirebaseFirestore.getInstance();

        db.collection("alumnos")
                .whereEqualTo(
                        "usuario",
                        alumno.getUsuario()
                )

                .get()

                .addOnSuccessListener(query -> {

                    if(!query.isEmpty()) {

                        String id =
                                query.getDocuments()
                                        .get(0)
                                        .getId();

                        db.collection("alumnos")
                                .document(id)
                                .delete()

                                .addOnSuccessListener(unused -> {

                                    listaAlumnos.remove(alumno);

                                    notifyDataSetChanged();
                                });
                    }
                });
    }

    private int calcularEdad(String fechaNacimiento) {

        try {

            String[] partes =
                    fechaNacimiento.split("/");

            int dia =
                    Integer.parseInt(partes[0]);

            int mes =
                    Integer.parseInt(partes[1]);

            int anio =
                    Integer.parseInt(partes[2]);

            Calendar fechaActual =
                    Calendar.getInstance();

            int edad =
                    fechaActual.get(Calendar.YEAR) - anio;

            int mesActual =
                    fechaActual.get(Calendar.MONTH) + 1;

            int diaActual =
                    fechaActual.get(Calendar.DAY_OF_MONTH);

            if(mes > mesActual ||
                    (mes == mesActual && dia > diaActual)) {

                edad--;
            }

            return edad;

        } catch (Exception e) {

            return 0;
        }
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvNombre, tvInstrumento;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvNombre =
                    itemView.findViewById(R.id.tvNombreAlumno);

            tvInstrumento =
                    itemView.findViewById(R.id.tvInfoAlumno);
        }
    }
}