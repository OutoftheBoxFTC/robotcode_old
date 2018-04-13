package org.ftc7244.robotcontroller.autonomous.terminators;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class ColorSensorTerminator extends Terminator {
    private final ColorSensor sensor;
    private final Color color;
    private final long successDuration;
    private long rangeEnterTime;
    public ColorSensorTerminator(ColorSensor sensor, Color color, long successDuration){
        this.sensor = sensor;
        this.color = color;
        this.successDuration = successDuration;
        rangeEnterTime = -1;
    }

    @Override
    public boolean shouldTerminate() {
        if(successDuration==0){
            return color.getColor(sensor)>color.threshold;
        }
        if(color.getColor(sensor)<color.threshold)
            rangeEnterTime=-1;
        else if(rangeEnterTime == -1)
            rangeEnterTime = System.currentTimeMillis();
        else if(System.currentTimeMillis()-rangeEnterTime>= successDuration)
            return true;
        return false;

    }

    public enum Color{
        RED(60) {
            @Override
            public int getColor(ColorSensor sensor) {
                return sensor.red();
            }
        },
        BLUE(50) {
            @Override
            public int getColor(ColorSensor sensor) {
                return sensor.blue();
            }
        };
        private int threshold;
        Color(int threshold){
            this.threshold = threshold;
        }
        public abstract int getColor(ColorSensor sensor);
    }
}
