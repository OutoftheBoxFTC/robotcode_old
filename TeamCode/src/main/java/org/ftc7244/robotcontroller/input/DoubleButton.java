package org.ftc7244.robotcontroller.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DoubleButton extends Button implements Runnable {
    private static volatile ExecutorService service;
    private static volatile boolean running;
    private volatile boolean pressed;
    private static final long WAIT_TIME = 200;

    public DoubleButton(ButtonType type, Gamepad gamepad){
        super(gamepad, type);
        pressed = false;
        if(service == null || service.isShutdown()) {
            service = Executors.newCachedThreadPool();
        }
        service.execute(this);
    }

    @Override
    public boolean isPressed(){
        if(pressed){
            pressed = false;
            return true;
        }
        return false;
    }

    public static void stop(){
        service.shutdown();
        running = false;
    }

    /*
    Do not call directly
     */
    @Override
    public void run() {
        while (running) {
            while (!super.isPressed());
            long lastTime = System.currentTimeMillis();
            while (super.isPressed()&&System.currentTimeMillis()-lastTime<=WAIT_TIME);
            while (!super.isPressed()&&System.currentTimeMillis()-lastTime<=WAIT_TIME);
            while (super.isPressed()&&System.currentTimeMillis()-lastTime<=WAIT_TIME);
            pressed = System.currentTimeMillis()-lastTime<=WAIT_TIME;
        }
    }
}
