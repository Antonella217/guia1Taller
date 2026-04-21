package videojuego;

import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    private JuegoPanel juegoPanel;

    public VentanaJuego() {
        setTitle("🚀 Videojuego - Batalla Espacial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        juegoPanel = new JuegoPanel();
        add(juegoPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Pedir foco para capturar teclas
        juegoPanel.requestFocusInWindow();

        // Iniciar el juego
        juegoPanel.iniciarJuego();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuego());
    }
}
