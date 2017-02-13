package org.ftc7244.robotcontroller.sensor.leds;


import android.graphics.Color;

/**
 * Created by brand on 2/13/2017.
 */

public class RGBStrip {
    private LedStrip red, green, blue;
    private int color;

    public RGBStrip(LedStrip red, LedStrip green, LedStrip blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        color = Color.BLACK;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        red.setChannel(Color.red(color));
        green.setChannel(Color.green(color));
        blue.setChannel(Color.blue(color));
    }

    public LedStrip getRed() {
        return red;
    }

    public void setRed(LedStrip red) {
        this.red = emptyCheck(red);
    }

    public LedStrip getGreen() {
        return green;
    }

    public void setGreen(LedStrip green) {
        this.green = emptyCheck(green);
    }

    public LedStrip getBlue() {
        return blue;
    }

    public void setBlue(LedStrip blue) {
        this.blue = emptyCheck(blue);
    }

    private LedStrip emptyCheck(LedStrip strip) {
        if (strip == null) return new EmptyStrip();
        return strip;
    }
}
