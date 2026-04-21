package videojuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.List;

public class MiNave extends Entidad implements Runnable {
    private static final int VELOCIDAD = 5;
    private boolean moviendoIzquierda = false;
    private boolean moviendoDerecha = false;
    private int limiteIzquierdo = 0;
    private int limiteDerecho = 800;
    private List<Proyectil> proyectiles;
    private Thread hilo;
    private long ultimoDisparo = 0;
    private static final long COOLDOWN_DISPARO = 300; // ms

    public MiNave(int x, int y) {
        super(x, y, 50, 40);
        proyectiles = new ArrayList<>();
    }

    @Override
    public void mover() {
        if (moviendoIzquierda && x > limiteIzquierdo) {
            x -= VELOCIDAD;
        }
        if (moviendoDerecha && x + ancho < limiteDerecho) {
            x += VELOCIDAD;
        }
    }

    public void disparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo >= COOLDOWN_DISPARO) {
            Proyectil p = new Proyectil(x + ancho / 2 - 3, y - 18);
            proyectiles.add(p);
            p.iniciarHilo();
            ultimoDisparo = ahora;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cuerpo principal de la nave
        GradientPaint grad = new GradientPaint(
            x, y, new Color(0, 200, 255),
            x, y + alto, new Color(0, 80, 180)
        );
        g2d.setPaint(grad);

        // Fuselaje central
        int[] xPuntos = {x + ancho/2, x + ancho - 5, x + ancho - 10, x + 10, x + 5};
        int[] yPuntos = {y, y + 20, y + alto, y + alto, y + 20};
        g2d.fillPolygon(xPuntos, yPuntos, 5);

        // Alas
        g2d.setColor(new Color(0, 150, 220));
        int[] xAlaIzq = {x + 10, x, x + 15};
        int[] yAlaIzq = {y + 20, y + alto, y + alto};
        g2d.fillPolygon(xAlaIzq, yAlaIzq, 3);

        int[] xAlaDer = {x + ancho - 10, x + ancho, x + ancho - 15};
        int[] yAlaDer = {y + 20, y + alto, y + alto};
        g2d.fillPolygon(xAlaDer, yAlaDer, 3);

        // Cabina
        g2d.setColor(new Color(150, 240, 255, 180));
        g2d.fillOval(x + ancho/2 - 8, y + 5, 16, 12);

        // Motor (llama)
        GradientPaint llama = new GradientPaint(
            x + ancho/2, y + alto, new Color(255, 200, 0),
            x + ancho/2, y + alto + 15, new Color(255, 50, 0, 0)
        );
        g2d.setPaint(llama);
        g2d.fillOval(x + ancho/2 - 8, y + alto - 5, 16, 20);
    }

    @Override
    public void run() {
        while (true) {
            mover();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void iniciarHilo() {
        hilo = new Thread(this);
        hilo.setDaemon(true);
        hilo.start();
    }

    public void setMoviendoIzquierda(boolean v) { this.moviendoIzquierda = v; }
    public void setMoviendoDerecha(boolean v) { this.moviendoDerecha = v; }
    public void setLimites(int izq, int der) { limiteIzquierdo = izq; limiteDerecho = der; }
    public List<Proyectil> getProyectiles() { return proyectiles; }
}
