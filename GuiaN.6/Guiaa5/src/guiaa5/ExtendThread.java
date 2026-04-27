/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package guiaa5;

/**
 *
 * @author Antonella
 */

    class HiloExtendThread extends Thread {

    HiloExtendThread() {
        super("Hilo demo");
        System.out.println("Hilo hijo: " + this);
        start();
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

public class ExtendThread {

    public static void main(String[] args) {
        new HiloExtendThread();

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
    

