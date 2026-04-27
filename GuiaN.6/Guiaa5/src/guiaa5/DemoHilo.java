/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guiaa5;

/**
 *
 * @author Antonella
 */
public class DemoHilo {

    public static void main(String[] args) {
        new NewThread();

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Hilo principal: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupción del hilo principal.");
        }

        System.out.println("Salida del hilo principal.");
    }
}

class NewThread implements Runnable {

    Thread t;

    NewThread() {
        t = new Thread(this, "Hilo demo");
        System.out.println("Hilo hijo: " + t);
        t.start();
    }

    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Hilo hijo: " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupción del hilo hijo.");
        }

        System.out.println("Salida del hilo hijo.");
    }
}