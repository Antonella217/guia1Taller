package videojuego;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class JuegoPanel extends JPanel implements Runnable, KeyListener {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;
    private static final int FPS = 60;

    // Entidades del juego
    private MiNave miNave;
    private List<NaveEnemiga> enemigos;

    // Estado del juego
    private boolean jugando = false;
    private boolean juegoTerminado = false;
    private int puntuacion = 0;
    private int vidas = 3;

    // Hilo del juego
    private Thread hiloJuego;

    // Estrellas del fondo
    private int[][] estrellas;
    private Random random = new Random();

    // Double buffering
    private BufferedImage buffer;
    private Graphics2D bufferG;

    public JuegoPanel() {
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        inicializarEstrellas();
        inicializarJuego();
    }

    private void inicializarEstrellas() {
        estrellas = new int[120][3];
        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = random.nextInt(ANCHO);
            estrellas[i][1] = random.nextInt(ALTO);
            estrellas[i][2] = random.nextInt(3) + 1; // brillo
        }
    }

    private void inicializarJuego() {
        miNave = new MiNave(ANCHO / 2 - 25, ALTO - 90);
        miNave.setLimites(0, ANCHO);

        enemigos = new ArrayList<>();

        NaveEnemiga enemigo1 = new NaveEnemiga(150, 80,
            new Color(200, 0, 50), new Color(255, 80, 100));
        enemigo1.setLimites(10, ANCHO - 10);

        NaveEnemiga enemigo2 = new NaveEnemiga(500, 140,
            new Color(100, 0, 200), new Color(180, 80, 255));
        enemigo2.setLimites(10, ANCHO - 10);

        enemigos.add(enemigo1);
        enemigos.add(enemigo2);

        puntuacion = 0;
        vidas = 3;
        juegoTerminado = false;
    }

    public void iniciarJuego() {
        jugando = true;

        // Iniciar hilo de la nave del jugador
        miNave.iniciarHilo();

        // Iniciar hilos de los enemigos
        for (NaveEnemiga e : enemigos) {
            e.iniciarHilo();
        }

        // Iniciar hilo principal del juego
        hiloJuego = new Thread(this);
        hiloJuego.setDaemon(true);
        hiloJuego.start();
    }

    @Override
    public void run() {
        long tiempoObjetivo = 1000 / FPS;
        while (jugando) {
            long inicio = System.currentTimeMillis();

            actualizar();
            repaint();

            long transcurrido = System.currentTimeMillis() - inicio;
            long espera = tiempoObjetivo - transcurrido;
            if (espera > 0) {
                try {
                    Thread.sleep(espera);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void actualizar() {
        if (juegoTerminado) return;

        // Mover estrellas (efecto parallax)
        for (int[] estrella : estrellas) {
            estrella[1] += estrella[2];
            if (estrella[1] > ALTO) {
                estrella[1] = 0;
                estrella[0] = random.nextInt(ANCHO);
            }
        }

        // Verificar colisiones proyectil-enemigo
        List<Proyectil> proyectiles = miNave.getProyectiles();
        Iterator<Proyectil> itP = proyectiles.iterator();
        while (itP.hasNext()) {
            Proyectil p = itP.next();
            if (!p.isActivo()) {
                itP.remove();
                continue;
            }

            for (NaveEnemiga enemigo : enemigos) {
                if (enemigo.isActivo() &&
                    p.getBounds().intersects(enemigo.getBounds())) {
                    p.setActivo(false);
                    enemigo.setActivo(false);
                    puntuacion += 100;
                    itP.remove();
                    break;
                }
            }
        }

        // Verificar si todos los enemigos fueron destruidos
        boolean todosDestruidos = true;
        for (NaveEnemiga e : enemigos) {
            if (e.isActivo()) {
                todosDestruidos = false;
                break;
            }
        }

        if (todosDestruidos && !juegoTerminado) {
            // Respawn de enemigos con mayor velocidad (bonus)
            for (NaveEnemiga e : enemigos) {
                e.setActivo(true);
            }
            puntuacion += 250; // bonus de oleada
        }

        // Verificar colisión nave-enemigo
        for (NaveEnemiga enemigo : enemigos) {
            if (enemigo.isActivo() &&
                miNave.getBounds().intersects(enemigo.getBounds())) {
                vidas--;
                if (vidas <= 0) {
                    juegoTerminado = true;
                    jugando = false;
                }
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo espacial
        dibujarFondo(g2d);

        if (!juegoTerminado) {
            // Dibujar proyectiles
            for (Proyectil p : miNave.getProyectiles()) {
                if (p.isActivo()) p.dibujar(g2d);
            }

            // Dibujar nave del jugador
            miNave.dibujar(g2d);

            // Dibujar enemigos
            for (NaveEnemiga e : enemigos) {
                if (e.isActivo()) e.dibujar(g2d);
            }

            // HUD
            dibujarHUD(g2d);
        } else {
            dibujarPantallaFin(g2d);
        }
    }

    private void dibujarFondo(Graphics2D g2d) {
        // Fondo con gradiente espacial
        GradientPaint fondo = new GradientPaint(
            0, 0, new Color(2, 5, 20),
            0, ALTO, new Color(5, 0, 30)
        );
        g2d.setPaint(fondo);
        g2d.fillRect(0, 0, ANCHO, ALTO);

        // Nebulosa sutil
        g2d.setColor(new Color(30, 0, 60, 30));
        g2d.fillOval(100, 50, 400, 250);
        g2d.setColor(new Color(0, 20, 80, 20));
        g2d.fillOval(400, 200, 350, 200);

        // Estrellas
        for (int[] estrella : estrellas) {
            int brillo = estrella[2];
            if (brillo == 1) {
                g2d.setColor(new Color(200, 200, 220, 160));
                g2d.fillRect(estrella[0], estrella[1], 1, 1);
            } else if (brillo == 2) {
                g2d.setColor(new Color(220, 220, 255, 200));
                g2d.fillRect(estrella[0], estrella[1], 2, 2);
            } else {
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.fillOval(estrella[0] - 1, estrella[1] - 1, 3, 3);
            }
        }

        // Línea divisoria inferior
        g2d.setColor(new Color(0, 150, 255, 40));
        g2d.fillRect(0, ALTO - 70, ANCHO, 1);
    }

    private void dibujarHUD(Graphics2D g2d) {
        // Panel superior semi-transparente
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRect(0, 0, ANCHO, 40);

        // Puntuación
        g2d.setFont(new Font("Courier New", Font.BOLD, 18));
        g2d.setColor(new Color(0, 220, 255));
        g2d.drawString("SCORE: " + puntuacion, 20, 27);

        // Vidas
        g2d.setColor(new Color(255, 80, 100));
        g2d.drawString("VIDAS: " + "♥ ".repeat(vidas), ANCHO - 170, 27);

        // Controles (pequeño)
        g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
        g2d.setColor(new Color(100, 100, 150));
        g2d.drawString("← → MOVER  |  ESPACIO: DISPARAR", ANCHO/2 - 130, 27);
    }

    private void dibujarPantallaFin(Graphics2D g2d) {
        // Overlay oscuro
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, ANCHO, ALTO);

        // Texto principal
        g2d.setFont(new Font("Courier New", Font.BOLD, 52));
        GradientPaint textoGrad = new GradientPaint(
            ANCHO/2 - 150, 0, new Color(255, 50, 80),
            ANCHO/2 + 150, 0, new Color(255, 150, 50)
        );
        g2d.setPaint(textoGrad);
        g2d.drawString("GAME OVER", ANCHO/2 - 155, ALTO/2 - 50);

        g2d.setFont(new Font("Courier New", Font.BOLD, 28));
        g2d.setColor(new Color(0, 220, 255));
        g2d.drawString("PUNTUACIÓN FINAL: " + puntuacion, ANCHO/2 - 155, ALTO/2 + 10);

        g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
        g2d.setColor(new Color(150, 150, 200));
        g2d.drawString("Presiona ENTER para jugar de nuevo", ANCHO/2 - 155, ALTO/2 + 60);
    }

    // ── KeyListener ──────────────────────────────────────────────────────────

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setMoviendoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setMoviendoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                if (!juegoTerminado) miNave.disparar();
                break;
            case KeyEvent.VK_ENTER:
                if (juegoTerminado) reiniciar();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setMoviendoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setMoviendoDerecha(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    private void reiniciar() {
        inicializarJuego();
        iniciarJuego();
    }
}
