package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre el Model, la Vista i
 * el Controlador.
 *
 * Aquestes notificacions permeten desacoblar les accions entre capes,
 * fent servir missatges per indicar què ha passat dins el sistema.
 *
 * @author tonitorres
 */
public enum Notificacio {
    /** Inicia la comparació de tots els idiomes amb un idioma d'origen. */
    COMPARAR_TOTS,
    /** Inicia la
     * comparació entre dos idiomes seleccionats. */
    COMPARAR_DOS,
    /** Notifica
     * que s'ha d'actualitzar el panell gràfic amb els resultats. */
    PINTAR_GRAF,
}
