package vista;

import controlador.Controlador;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

/**
 * Panell central de la GUI que representa visualment el graf de ciutats
 * i la ruta òptima trobada amb Branch and Bound per al problema del TSP.
 *
 * Permet seleccionar la ciutat inicial fent clic en un node.
 *
 * @author tonitorres
 */
public class PanellGraf extends JPanel {

    private int[][] matriu;
    private List<Integer> camiOptim;
    private boolean mostrarCostos = false;
    private static final int RADIUS = 200;

    private Point[] posicions = new Point[0];
    private final Controlador controlador;

    public PanellGraf(Controlador controlador) {
        this.controlador = controlador;
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (matriu == null) return;
                for (int i = 0; i < posicions.length; i++) {
                    Point p = posicions[i];
                    if (p.distance(e.getPoint()) <= 20) {
                        controlador.getModel().setCiutatInicial(i);
                        repaint();
                        break;
                    }
                }
            }
        });
    }

    public void actualitzarGraf(int[][] matriu) {
        this.matriu = matriu;
        this.camiOptim = null;
        repaint();
    }

    public void mostrarCami(List<Integer> cami) {
        this.camiOptim = cami;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matriu == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int n = matriu.length;
        posicions = new Point[n];
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = (int) (cx + RADIUS * Math.cos(angle));
            int y = (int) (cy + RADIUS * Math.sin(angle));
            posicions[i] = new Point(x, y);
        }

        Font fontOriginal = g2.getFont();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && matriu[i][j] < Integer.MAX_VALUE / 2) {
                    g2.setColor(new Color(200, 200, 200));
                    g2.setStroke(new BasicStroke(1.0f));
                    dibuixarFletxa(g2, posicions[i], posicions[j]);

                    if (mostrarCostos) {
                        int mx = (posicions[i].x + posicions[j].x) / 2;
                        int my = (posicions[i].y + posicions[j].y) / 2;
                        int dx = posicions[j].x - posicions[i].x;
                        int dy = posicions[j].y - posicions[i].y;
                        double length = Math.sqrt(dx * dx + dy * dy);
                        int offsetX = (int) (-dy / length * 10);
                        int offsetY = (int) (dx / length * 10);

                        String costText = String.valueOf(matriu[i][j]);
                        g2.setColor(Color.DARK_GRAY);
                        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                        g2.drawString(costText, mx + offsetX, my + offsetY);
                    }
                }
            }
        }

        g2.setFont(fontOriginal);

        if (camiOptim != null && camiOptim.size() > 1) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2.5f));
            for (int i = 0; i < camiOptim.size() - 1; i++) {
                int from = camiOptim.get(i);
                int to = camiOptim.get(i + 1);
                dibuixarFletxa(g2, posicions[from], posicions[to]);
            }
        }

        int ciutatInicial = controlador.getModel().getCiutatInicial();
        for (int i = 0; i < n; i++) {
            Point p = posicions[i];

            g2.setColor(i == ciutatInicial ? Color.GREEN : Color.WHITE);
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 20, p.y - 20, 40, 40);

            String nom = String.valueOf((char) ('A' + i));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(nom);
            int textHeight = fm.getAscent();
            g2.drawString(nom, p.x - textWidth / 2, p.y + textHeight / 2 - 2);
        }
    }

    private void dibuixarFletxa(Graphics2D g2, Point p1, Point p2) {
        double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        int longitud = 20;
        int xStart = (int) (p1.x + longitud * Math.cos(angle));
        int yStart = (int) (p1.y + longitud * Math.sin(angle));
        int xEnd = (int) (p2.x - longitud * Math.cos(angle));
        int yEnd = (int) (p2.y - longitud * Math.sin(angle));

        g2.drawLine(xStart, yStart, xEnd, yEnd);

        int midaFletxa = 10;
        int x1 = (int) (xEnd - midaFletxa * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (yEnd - midaFletxa * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (xEnd - midaFletxa * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (yEnd - midaFletxa * Math.sin(angle + Math.PI / 6));

        Polygon punta = new Polygon();
        punta.addPoint(xEnd, yEnd);
        punta.addPoint(x1, y1);
        punta.addPoint(x2, y2);
        g2.fillPolygon(punta);
    }

    public void setMostrarCostos(boolean mostrar) {
        this.mostrarCostos = mostrar;
        repaint();
    }
}
