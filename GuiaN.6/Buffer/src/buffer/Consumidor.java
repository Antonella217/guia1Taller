/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buffer;

/**
 *
 * @author Antonella
 */
public class Consumidor extends Thread {

    private Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            int numero = buffer.consumir();
            int resultado = numero * 2;

            System.out.println("Consumidor leyo: " + numero 
                    + " | Multiplicado por 2 = " + resultado);
            System.out.println("-----------------------------------");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Consumidor interrumpido.");
            }
        }
    }
}