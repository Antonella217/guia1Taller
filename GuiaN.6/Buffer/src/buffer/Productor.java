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

public class Productor extends Thread {

    private Buffer buffer;
    private Random random;

    public Productor(Buffer buffer) {
        this.buffer = buffer;
        this.random = new Random();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            int numero = random.nextInt(100) + 1;
            buffer.producir(numero);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Productor interrumpido.");
            }
        }
    }
}