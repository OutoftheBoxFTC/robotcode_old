package org.ftc7244.robotcontroller.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.util.RobotLog;

/**
 * Created by OOTB on 11/11/2016.
 */
public abstract class BeaconAutonomous extends PIDAutonomous {

    public boolean isColor(int color) {
        RobotLog.ii("Color", robot.getBeaconSensor().blue() + ":" + robot.getBeaconSensor().red());
        switch (color) {
            case Color.BLUE:
                return robot.getBeaconSensor().blue() > robot.getBeaconSensor().red();
            case Color.RED:
                RobotLog.ii("Color", "We are reading red");
                return robot.getBeaconSensor().blue() < robot.getBeaconSensor().red();
            default:
                RobotLog.ee("ERROR", "Color does not exist!");
                return false;
        }
    }

    public void pushBeacon() throws InterruptedException {
        robot.getBeaconPusher().setPosition(0);
        sleep(750);
        robot.getBeaconPusher().setPosition(1);
        sleep(750);
    }
}
