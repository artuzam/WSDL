package com.company;

public class Persona {
    String nombre;
    int edad;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }

    public String infoPersona() {
        return "Nombre: " + nombre + ", edad: " + edad;
    }
}
