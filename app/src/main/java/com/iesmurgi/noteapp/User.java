package com.iesmurgi.noteapp;

public class User {
    String email;
    String password;
    int id;
    Nota[] notas;
    Categoria[] categorias;
    String phone;

    public User(){

    }
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Categoria[] getCategorias() {
        return categorias;
    }

    public void setCategorias(Categoria[] categorias) {
        this.categorias = categorias;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nota[] getNotes() {
        return notas;
    }

    public void setNotes(Nota[] notas) {
        this.notas = notas;
    }
}
