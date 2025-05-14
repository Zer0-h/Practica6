package vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import model.ResultatComparacio;

/**
 * Panell que representa visualment un graf de distàncies lèxiques entre
 * idiomes.
 * El panell permet fer zoom amb la roda del ratolí i arrossegar per
 * desplaçar-se.
 * Els idiomes es col·loquen en posició circular al voltant del node origen.
 *
 * Cada aresta mostra la distància calculada, i els nodes es dibuixen com
 * cercles.
 *
 * @author tonitorres
 */
public class PanellGraf extends JPanel {

    /** Llista de resultats de comparació a visualitzar. */
    private List<ResultatComparacio> resultats;

    /** Escala de zoom actual del panell. */
    private double escala = 1.0;

    /** Punt inicial de l’arrossegament amb el ratolí. */
    private Point arrossegantDesDe = null;

    /** Desplaçament horitzontal i vertical del panell. */
    private int offsetX = 0, offsetY = 0;

    /** Codi de l'idioma origen a centrar. */
    private String idiomaOrigen;

    /**
     * Constructor del panell. Configura les opcions de zoom i arrossegament.
     */
    public PanellGraf() {
        this.resultats = new ArrayList<>();
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);

        // Zoom amb scroll
        addMouseWheelListener(e -> {
            escala *= (e.getWheelRotation() < 0) ? 1.1 : 0.9;
            escala = Math.max(0.2, Math.min(escala, 5.0));
            repaint();
        });

        // Inici de l’arrossegament
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                arrossegantDesDe = e.getPoint();
            }
        });

        // Arrossegament en moviment
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (arrossegantDesDe != null) {
                    int dx = e.getX() - arrossegantDesDe.x;
                    int dy = e.getY() - arrossegantDesDe.y;
                    offsetX += dx;
                    offsetY += dy;
                    arrossegantDesDe = e.getPoint();
                    repaint();
                }
            }
        });
    }

    /**
     * Assigna els resultats i l'idioma origen per pintar el graf.
     *
     * @param resultats    llista de distàncies a representar
     * @param idiomaOrigen codi de l'idioma central
     */
    public void pintarResultats(List<ResultatComparacio> resultats, String idiomaOrigen) {
        this.resultats = resultats;
        this.idiomaOrigen = idiomaOrigen;
        repaint();
    }

    /**
     * Mètode principal de dibuix del panell. Representa nodes, arestes i
     * etiquetes.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (resultats == null || resultats.isEmpty()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Aplicam zoom i desplaçament
        g2.translate(offsetX, offsetY);
        g2.scale(escala, escala);

        // Reunim els idiomes únics implicats
        Set<String> noms = new HashSet<>();
        for (ResultatComparacio r : resultats) {
            noms.add(r.getIdiomaA());
            noms.add(r.getIdiomaB());
        }

        int centreX = getWidth() / 2;
        int centreY = getHeight() / 2;
        int radi = Math.min(centreX, centreY) - 100;
        radi = Math.max(radi, 120);

        Map<String, Point> posicionsMap = new HashMap<>();

        // Centram l'idioma origen
        posicionsMap.put(idiomaOrigen, new Point(centreX, centreY));

        // Distribuïm els altres idiomes circularment
        List<String> restants = noms.stream()
                .filter(n -> !n.equals(idiomaOrigen))
                .toList();

        for (int i = 0; i < restants.size(); i++) {
            double angle = 2 * Math.PI * i / restants.size();
            int x = centreX + (int) (radi * Math.cos(angle));
            int y = centreY + (int) (radi * Math.sin(angle));
            posicionsMap.put(restants.get(i), new Point(x, y));
        }

        // Dibuixam les arestes amb la distància
        for (ResultatComparacio r : resultats) {
            Point p1 = posicionsMap.get(r.getIdiomaA());
            Point p2 = posicionsMap.get(r.getIdiomaB());

            if (p1 != null && p2 != null) {
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                int mx = (p1.x + p2.x) / 2;
                int my = (p1.y + p2.y) / 2;

                // Posició lleugerament desplaçada per no solapar amb la línia
                int dx = p2.x - p1.x;
                int dy = p2.y - p1.y;
                double length = Math.sqrt(dx * dx + dy * dy);
                double offsetX = -dy / length * 12;
                double offsetY = dx / length * 12;

                int labelX = (int) (mx + offsetX);
                int labelY = (int) (my + offsetY);

                String text = String.format("%.3f", r.getDistancia());
                int w = g2.getFontMetrics().stringWidth(text);
                g2.setColor(Color.BLACK);
                g2.drawString(text, labelX - w / 2, labelY);
            }
        }

        // Dibuixam els nodes (cercles amb nom al centre)
        for (Map.Entry<String, Point> entry : posicionsMap.entrySet()) {
            Point p = entry.getValue();
            String nom = entry.getKey();
            int rNode = 25;

            // Color diferent per l'idioma origen
            if (nom.equals(idiomaOrigen)) {
                g2.setColor(new Color(0, 180, 255));
            } else {
                g2.setColor(Color.LIGHT_GRAY);
            }

            g2.fillOval(p.x - rNode, p.y - rNode, 2 * rNode, 2 * rNode);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - rNode, p.y - rNode, 2 * rNode, 2 * rNode);

            // Centrar el text dins el node
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(nom);
            int textHeight = fm.getAscent();
            g2.drawString(nom, p.x - textWidth / 2, p.y + textHeight / 2 - 2);
        }
    }
}
