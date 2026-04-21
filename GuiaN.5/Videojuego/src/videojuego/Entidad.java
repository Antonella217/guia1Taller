package videojuego;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entidad {
    protected int x, y;
    protected int ancho, alto;
    protected boolean activo;

    public Entidad(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.activo = true;
    }

    public abstract void mover();
    public abstract void dibujar(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
