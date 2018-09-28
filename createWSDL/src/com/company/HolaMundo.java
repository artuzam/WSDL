package com.company;

public class HolaMundo {
    public String salude(String nombre) {
        return "Hola " + nombre;
    }

    public int sumar(int a, int b) {
        return  a+b;
    }

    public String infoPersona(Persona persona) { return persona.infoPersona(); }
}