package videojuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

public class Proyectil extends Entidad implements Runnable {
    private static final int VELOCIDAD = 6;
    private Thread hilo;

    public Proyectil(int x, int y) {
        super(x, y, 6, 18);
    }

    @Override
    public void mover() {
        y -= VELOCIDAD;
        if (y < 0) {
            activo = false;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        if (!activo) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Brillo del proyectil
        GradientPaint gradiente = new GradientPaint(
            x, y, new Color(255, 255, 150),
            x, y + alto, new Color(255, 100, 0)
        );
        g2d.setPaint(gradiente);
        g2d.fillRoundRect(x, y, ancho, alto, 3, 3);

        // Halo
        g2d.setColor(new Color(255, 200, 0, 80));
        g2d.fillOval(x - 3, y - 2, ancho + 6, 12);
    }

    @Override
    public void run() {
        while (activo) {
            mover();
            try {
                Thread.sleep(16); // ~60 fps
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
}
