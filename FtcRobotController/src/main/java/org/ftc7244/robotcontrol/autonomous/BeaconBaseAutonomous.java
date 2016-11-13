package org.ftc7244.robotcontrol.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by OOTB on 11/11/2016.
 */
public abstract class BeaconBaseAutonomous extends EncoderBaseAutonomous {

    protected void pushBeaconUntil(BeaconColor color) throws InterruptedException {
        pushBeacon();
        if (getColor(color) < 10) {
            sleep(4250);
            pushBeacon();
        }
    }

    private void pushBeacon() throws InterruptedException {
        robot.getBeaconPusher().setPosition(0);
        sleep(750);
        robot.getBeaconPusher().setPosition(1);
        sleep(750);
    }

    private int getColor(BeaconColor color) {
        return color == BeaconColor.BLUE ? robot.getBeaconSensor().blue() : robot.getBeaconSensor().red();
    }

    protected enum BeaconColor {
        RED,
        BLUE
    }
}
