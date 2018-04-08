package org.ftc7244.robotcontroller.autonomous.terminators;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorTerminator extends Terminator {
    private final ColorSensor sensor;
    private final int color, threshold;
    private final long successDuration;
    private long rangeEnterTime;

    public ColorSensorTerminator(ColorSensor sensor, int color, int threshold, long successDuration){
        this.sensor = sensor;
        this.color = color;
        this.threshold = threshold;
        this.successDuration = successDuration;
        rangeEnterTime = -1;
    }

    @Override
    public boolean shouldTerminate() {
        if(getColor()<threshold)
            rangeEnterTime=-1;
        else if(rangeEnterTime == -1)
            rangeEnterTime = System.currentTimeMillis();
        else if(System.currentTimeMillis()-rangeEnterTime>= successDuration)
            return true;
        return false;
    }

    private int getColor(){
        return color==Color.RED?sensor.red():sensor.blue();
    }
}
