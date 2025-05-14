package model;

import java.util.List;

/**
 * Classe que representa un idioma, format pel seu codi identificador
 * i la llista de paraules que el componen.
 *
 * Aquesta classe encapsula la informació bàsica d'un diccionari d'idioma
 * utilitzat per a les comparacions lèxiques.
 *
 * @author tonitorres
 */
public class Idioma {

    /** Codi de l'idioma (per exemple: "cat", "esp", "eng"). */
    private final String nom;

    /** Llista de paraules que formen el diccionari de l'idioma. */
    private final List<String> paraules;

    /**
     * Constructor de la classe Idioma.
     *
     * @param nom      Codi identificador de l'idioma
     * @param paraules Llista de paraules del diccionari
     */
    public Idioma(String nom, List<String> paraules) {
        this.nom = nom;
        this.paraules = paraules;
    }

    /**
     * Retorna el codi de l'idioma.
     *
     * @return nom curt de l'idioma
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retorna la llista de paraules d'aquest idioma.
     *
     * @return llista de paraules
     */
    public List<String> getParaules() {
        return paraules;
    }
}
