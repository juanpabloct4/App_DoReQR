package com.example.doreqr;

import com.google.firebase.Timestamp;

public class Asistencia {

    String nombreAlumno;
    String hora;
    String fecha;
    String idAlumno;

    Timestamp timestamp;

    public Asistencia() {
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public String getHora() {
        return hora;
    }

    public String getFecha() {
        return fecha;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}