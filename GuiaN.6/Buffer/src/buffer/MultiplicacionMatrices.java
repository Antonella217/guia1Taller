/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buffer;

/**
 *
 * @author Antonella
 */
import java.util.Random;

public class MultiplicacionMatrices {

    static final int TAM = 500;

    static int[][] matrizA = new int[TAM][TAM];
    static int[][] matrizB = new int[TAM][TAM];
    static int[][] resultadoSecuencial = new int[TAM][TAM];
    static int[][] resultadoParalelo = new int[TAM][TAM];

    public static void main(String[] args) {

        llenarMatrices();

        long inicioSecuencial = System.currentTimeMillis();

        multiplicarSecuencial();

        long finSecuencial = System.currentTimeMillis();

        long tiempoSecuencial = finSecuencial - inicioSecuencial;

        System.out.println("Tiempo version secuencial: " 
                + tiempoSecuencial + " milisegundos");

        long inicioParalelo = System.currentTimeMillis();

        Thread hilo1 = new Thread(new Multiplicador(0, TAM / 4));
        Thread hilo2 = new Thread(new Multiplicador(TAM / 4, TAM / 2));
        Thread hilo3 = new Thread(new Multiplicador(TAM / 2, (TAM * 3) / 4));
        Thread hilo4 = new Thread(new Multiplicador((TAM * 3) / 4, TAM));

        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        try {
            hilo1.join();
            hilo2.join();
            hilo3.join();
            hilo4.join();
        } catch (InterruptedException e) {
            System.out.println("Error al esperar los hilos.");
        }

        long finParalelo = System.currentTimeMillis();

        long tiempoParalelo = finParalelo - inicioParalelo;

        System.out.println("Tiempo version con 4 Threads: " 
                + tiempoParalelo + " milisegundos");

        verificarResultados();
    }

    public static void llenarMatrices() {
        Random random = new Random();

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                matrizA[i][j] = random.nextInt(10);
                matrizB[i][j] = random.nextInt(10);
            }
        }
    }

    public static void multiplicarSecuencial() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                for (int k = 0; k < TAM; k++) {
                    resultadoSecuencial[i][j] += matrizA[i][k] * matrizB[k][j];
                }
            }
        }
    }

    static class Multiplicador implements Runnable {

        private int filaInicio;
        private int filaFin;

        public Multiplicador(int filaInicio, int filaFin) {
            this.filaInicio = filaInicio;
            this.filaFin = filaFin;
        }

        @Override
        public void run() {
            for (int i = filaInicio; i < filaFin; i++) {
                for (int j = 0; j < TAM; j++) {
                    for (int k = 0; k < TAM; k++) {
                        resultadoParalelo[i][j] += matrizA[i][k] * matrizB[k][j];
                    }
                }
            }
        }
    }

    public static void verificarResultados() {
        boolean iguales = true;

        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (resultadoSecuencial[i][j] != resultadoParalelo[i][j]) {
                    iguales = false;
                    break;
                }
            }
        }

        if (iguales) {
            System.out.println("Los resultados de ambas versiones son iguales.");
        } else {
            System.out.println("Los resultados son diferentes.");
        }
    }
}