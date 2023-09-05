package com.lucadepalo.personalgarden;

/**
 * Questa classe rappresenta un utente dell'applicazione.
 * Contiene le informazioni essenziali di un utente, come ID, nome utente, email, nome e cognome.
 */
public class User {

    // Identificativo univoco dell'utente.
    private int id;

    // Nome utente dell'utente.
    private String username;

    // Indirizzo email dell'utente.
    private String email;

    // Nome dell'utente.
    private String nome;

    // Cognome dell'utente.
    private String cognome;

    /**
     * Costruttore della classe User.
     * Inizializza un nuovo utente con i dettagli forniti.
     *
     * @param id Identificativo univoco dell'utente.
     * @param username Nome utente dell'utente.
     * @param email Indirizzo email dell'utente.
     * @param nome Nome dell'utente.
     * @param cognome Cognome dell'utente.
     */
    public User(int id, String username, String email, String nome, String cognome) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Restituisce l'identificativo univoco dell'utente.
     *
     * @return ID dell'utente.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il nome utente dell'utente.
     *
     * @return Nome utente.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return Email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }
}
