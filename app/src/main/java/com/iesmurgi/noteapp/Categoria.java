package com.iesmurgi.noteapp;

import java.util.ArrayList;

public class Categoria {
    String nombre;
    String icon;
    ArrayList<Nota> notas;

    public Categoria(){

    }
    public Categoria(String nombre, String icon, ArrayList<Nota> notas){
        this.nombre = nombre;
        this.icon = icon;
        this.notas = notas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<Nota> getNotes() {
        return notas;
    }

    public void setNotes(ArrayList<Nota> notas) {
        this.notas = notas;
    }
}
