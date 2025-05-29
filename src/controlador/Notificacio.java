package controlador;

/**
 * Enumeració que defineix els diferents tipus de notificacions utilitzades
 * en el patró d'esdeveniments per comunicar canvis entre la Vista, el Model
 * i el Controlador.
 *
 * Aquest sistema permet una arquitectura desacoblada, on cada capa actua
 * en funció de l’esdeveniment rebut.
 *
 * @author tonitorres
 */
public enum Notificacio {

    /** Notificació per generar un nou graf aleatori. */
    GENERAR_GRAF,
    /** Notificació
     * per iniciar el càlcul del TSP amb Branch and Bound. */
    RESOLDRE_TSP,
    /** Notificació
     * per pintar els resultats obtinguts (cost, ruta, estadístiques). */
    PINTAR_RESULTAT,
    /** Notificació
     * per dibuixar el graf actual (amb nodes i arestes). */
    PINTAR_GRAF
}
