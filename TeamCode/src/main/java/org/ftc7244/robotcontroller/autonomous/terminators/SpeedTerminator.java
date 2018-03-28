package org.ftc7244.robotcontroller.autonomous.terminators;

import org.ftc7244.robotcontroller.hardware.Hardware;

public class SpeedTerminator extends Terminator {
    private final Hardware robot;
    private final long successTime;
    private final double speedRange;

    private long lastTimeStamp, currentSuccessTime;
    private int lastEncoderAvg;
    public SpeedTerminator(double speedRange, long successTime, Hardware robot){
        this.successTime = successTime;
        this.speedRange = speedRange;
        this.robot = robot;
        lastEncoderAvg = robot.getDriveEncoderAverage();
    }

    @Override
    public boolean shouldTerminate() {
        long elapsedTime = System.currentTimeMillis() - lastTimeStamp;
        lastTimeStamp = System.currentTimeMillis();
        int encoderAverage = robot.getDriveEncoderAverage();
        double speed = (encoderAverage-lastEncoderAvg)/elapsedTime;
        if(Math.abs(speed)<speedRange)
                currentSuccessTime += elapsedTime;
        return currentSuccessTime >= successTime;
    }

    @Override
    public void terminated(boolean status) {
        currentSuccessTime = successTime;
    }
}
