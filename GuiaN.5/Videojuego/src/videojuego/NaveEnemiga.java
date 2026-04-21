package videojuego;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;

public class NaveEnemiga extends Entidad implements Runnable {
    private static final int VELOCIDAD = 2;
    private int direccion = 1; // 1 = derecha, -1 = izquierda
    private int limiteIzquierdo = 10;
    private int limiteDerecho = 750;
    private Thread hilo;
    private Color colorPrimario;
    private Color colorSecundario;
    private int oscilacion = 0;

    public NaveEnemiga(int x, int y, Color colorPrimario, Color colorSecundario) {
        super(x, y, 48, 38);
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
    }

    @Override
    public void mover() {
        x += VELOCIDAD * direccion;
        oscilacion++;
        // Leve movimiento vertical sinusoidal
        y += (int)(Math.sin(oscilacion * 0.05) * 0.8);

        if (x + ancho >= limiteDerecho) {
            direccion = -1;
        }
        if (x <= limiteIzquierdo) {
            direccion = 1;
        }
    }

    @Override
    public void dibujar(Graphics g) {
        if (!activo) return;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cuerpo de la nave enemiga (forma de disco/platillo)
        GradientPaint grad = new GradientPaint(
            x, y, colorSecundario,
            x, y + alto, colorPrimario
        );
        g2d.setPaint(grad);

        // Cúpula superior
        g2d.fillOval(x + 8, y, ancho - 16, 20);

        // Base ancha
        g2d.fillOval(x, y + 12, ancho, alto - 12);

        // Brillo de cabina
        g2d.setColor(new Color(255, 255, 200, 160));
        g2d.fillOval(x + 14, y + 3, 20, 12);

        // Luces inferiores
        Color[] luces = {Color.RED, Color.YELLOW, Color.GREEN};
        for (int i = 0; i < luces.length; i++) {
            int parpadeo = (oscilacion / 8 + i) % 3;
            g2d.setColor(parpadeo == 0 ? luces[i] : luces[i].darker().darker());
            g2d.fillOval(x + 8 + i * 12, y + alto - 8, 8, 8);
        }

        // Borde metálico
        g2d.setColor(colorPrimario.darker());
        g2d.drawOval(x, y + 12, ancho, alto - 12);
        g2d.drawOval(x + 8, y, ancho - 16, 20);
    }

    @Override
    public void run() {
        while (activo) {
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

    public void setLimites(int izq, int der) {
        limiteIzquierdo = izq;
        limiteDerecho = der;
    }
}
