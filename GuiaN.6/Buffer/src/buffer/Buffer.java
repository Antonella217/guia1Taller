/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package buffer;

/**
 *
 * @author Antonella
 */
public class Buffer {

    private int numero;
    private boolean disponible = false;

    public synchronized void producir(int numero) {
        while (disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error en productor.");
            }
        }

        this.numero = numero;
        disponible = true;

        System.out.println("Productor genero: " + numero);

        notify();
    }

    public synchronized int consumir() {
        while (!disponible) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error en consumidor.");
            }
        }

        disponible = false;
        notify();

        return numero;
    }
}