package com.example.noteapp;

import android.graphics.drawable.Icon;

import java.util.Date;

public class Note {
    int id;
    String titulo;
    String contenido;
    boolean vence;
    Date fechaVencimiento;
    Icon icon;
    String tipo;

    public Note(){

    }
    public Note(String titulo, String contenido, boolean vence, Date fechaVencimiento, String tipo){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public boolean isVence() {
        return vence;
    }

    public void setVence(boolean vence) {
        this.vence = vence;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
