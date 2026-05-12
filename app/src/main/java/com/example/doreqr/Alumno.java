package com.example.doreqr;

public class Alumno {
    String nombre;
    String usuario;
    String password;
    String telefono;
    String fechaNacimiento;
    String sexo;
    String instrumento;

    public Alumno(){
    }

    public Alumno(String nombre, String usuario, String password,
                  String telefono, String fechaNacimiento,
                  String sexo, String instrumento){
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.instrumento = instrumento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public String getInstrumento() {
        return instrumento;
    }
}
