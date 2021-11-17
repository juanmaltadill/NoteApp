package com.example.noteapp;

import android.graphics.drawable.Icon;

import java.util.ArrayList;

public class Categoria {
    String nombre;
    Icon icon;
    ArrayList<Note> notes;

    public Categoria(){

    }
    public Categoria(String nombre, Icon icon, ArrayList<Note> notes){
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

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
