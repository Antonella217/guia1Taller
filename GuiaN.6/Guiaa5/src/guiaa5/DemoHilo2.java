/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guiaa5;

/**
 *
 * @author Antonella
 */
public class DemoHilo2 {

    public static void main(String[] args) {
        Thread t = Thread.currentThread();

        System.out.println("Hilo actual: " + t);

        t.setName("Mi Hilo");
        System.out.println("Después del cambio de nombre: " + t);

        try {
            for (int n = 5; n > 0; n--) {
                System.out.println(n);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupción del hilo principal.");
        }
    }
}