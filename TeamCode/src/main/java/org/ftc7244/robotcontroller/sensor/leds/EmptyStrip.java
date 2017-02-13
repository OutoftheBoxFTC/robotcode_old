package org.ftc7244.robotcontroller.sensor.leds;

import com.qualcomm.robotcore.hardware.PWMOutput;

/**
 * Created by brand on 2/13/2017.
 */

public class EmptyStrip extends LedStrip {
    public EmptyStrip() {
        super(null);
    }

    @Override
    public void setChannel(int channel) {
        this.channel = channel;
    }
}
