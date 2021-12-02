package com.juanmaltadill.noteapp;

import android.graphics.drawable.Icon;

import java.util.ArrayList;

public class Categoria {
    String nombre;
    String icon;
    ArrayList<Note> notes;

    public Categoria(){

    }
    public Categoria(String nombre, String icon, ArrayList<Note> notes){
        this.nombre = nombre;
        this.icon = icon;
        this.notes = notes;
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

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
