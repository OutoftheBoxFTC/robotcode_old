package org.ftc7244.robotcontroller.sensor.leds;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputImpl;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by brand on 2/13/2017.
 */

public class LedStrip implements HardwareDevice {
    protected PWMOutput pwm;
    protected int channel;

    public LedStrip(PWMOutput pwm) {
        this.pwm = pwm;
        this.channel = 0;
    }

    public void setChannel(int channel) {
        channel = Range.clip(channel, 0, 255);
        pwm.setPulseWidthPeriod(channel);
    }

    public int getChannel() {
        return channel;
    }

    public HardwareDevice.Manufacturer getManufacturer() {
        return Manufacturer.Other;
    }

    @Override
    public String getDeviceName() {
        return "Led Strip";
    }

    @Override
    public String getConnectionInfo() {
        return getDeviceName() + " = " + getChannel();
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        pwm.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        pwm.close();
    }
}
