package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Model del patró MVC.
 * Encapsula l’estat actual de l’aplicació, incloent-hi la selecció
 * d’idiomes, els resultats de comparacions i la càrrega de diccionaris.
 *
 * Aquesta classe no conté lògica de comparació, només gestiona dades.
 *
 * @author tonitorres
 */
public class Model {

    /** Idioma origen seleccionat per a la comparació. */
    private String idiomaOrigen;

    /** Idioma destí seleccionat (en cas de comparació puntual). */
    private String idiomaDesti;

    /** Llista de resultats de comparacions realitzades. */
    private List<ResultatComparacio> resultats;

    /**
     * Defineix els idiomes seleccionats per a una comparació.
     *
     * @param iOrigen codi de l'idioma origen
     * @param iDesti  codi de l'idioma destí
     */
    public void setIdiomes(String iOrigen, String iDesti) {
        idiomaOrigen = iOrigen;
        idiomaDesti = iDesti;
    }

    /**
     * Retorna l'idioma origen actual.
     */
    public String getIdiomaOrigen() {
        return idiomaOrigen;
    }

    /**
     * Retorna l'idioma destí actual.
     */
    public String getIdiomaDesti() {
        return idiomaDesti;
    }

    /**
     * Retorna la llista de resultats múltiples de comparació.
     */
    public List<ResultatComparacio> getResultatsMultiples() {
        return resultats;
    }

    /**
     * Neteja la llista de resultats per començar una nova comparació.
     */
    public void resetResultats() {
        resultats = new ArrayList<>();
    }

    /**
     * Assigna directament una nova llista de resultats.
     *
     * @param r nova llista de resultats
     */
    public void setResultats(List<ResultatComparacio> r) {
        resultats = r;
    }

    /**
     * Afegeix un resultat individual a la llista.
     *
     * @param r resultat a afegir
     */
    public void afegirResultats(ResultatComparacio r) {
        resultats.add(r);
    }

    /**
     * Carrega els diccionaris dels idiomes indicats des de la carpeta
     * "diccionaris".
     *
     * @param idiomes llista de codis d'idioma
     *
     * @return llista d'objectes Idioma amb les paraules carregades
     */
    public List<Idioma> carregarDiccionaris(List<String> idiomes) {
        List<Idioma> diccionaris = new ArrayList<>();

        File carpeta = new File("diccionaris");

        File[] fitxers = carpeta.listFiles((dir, name)
                -> idiomes.contains(name.replace(".txt", ""))
        );

        for (File fitxer : fitxers) {
            try {
                System.out.println("Carregant el fitxer " + fitxer.getName());

                List<String> paraules = Files.readAllLines(fitxer.toPath());
                String nom = fitxer.getName().substring(0, 3);
                diccionaris.add(new Idioma(nom, paraules));

                System.out.println("Carregat!");
            } catch (IOException e) {
                System.out.println("Error llegint el fitxer: " + fitxer.getName());
            }
        }

        return diccionaris;
    }
}
