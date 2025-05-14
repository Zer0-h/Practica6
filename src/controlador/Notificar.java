package controlador;

/**
 * Interfície que defineix el patró d'esdeveniments per a la comunicació entre
 * el Model, la Vista i el Controlador dins l'arquitectura MVC.
 *
 * Aquesta interfície permet que qualsevol component pugui rebre notificacions
 * del sistema de manera desacoblada, facilitant la gestió de l'estat i les
 * interaccions entre capes.
 *
 * Les classes que implementin aquesta interfície hauran de gestionar
 * notificacions per reaccionar a esdeveniments concrets (com ara completament
 * de processos, errors, actualització de dades, etc.).
 *
 * @author tonitorres
 */
public interface Notificar {

    /**
     * Mètode que rep una notificació per part d'una altra capa del sistema.
     *
     * @param n La notificació a processar.
     */
    void notificar(Notificacio n);
}
