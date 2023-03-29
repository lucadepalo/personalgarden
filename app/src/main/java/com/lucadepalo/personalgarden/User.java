package com.lucadepalo.personalgarden;

public class User {

    private int id;
    private String username, email, nome, cognome;

    public User(int id, String username, String email, String nome, String cognome) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    public int getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }

    public String getEmail() {

        return email;
    }

    public String getNome() {

        return nome;
    }

    public String getCognome() {

        return cognome;
    }
}