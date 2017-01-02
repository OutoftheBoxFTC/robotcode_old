package org.ftc7244.robotcontrol;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.autonomous.BaseAutonomous;
import org.ftc7244.robotcontrol.autonomous.BeaconBaseAutonomous;

/**
 * Created by OOTB on 11/12/2016.
 */
//
@Autonomous(name="Beacon Blue")
public class BeaconBlue extends BaseAutonomous {

    @Override
    public void run() throws InterruptedException {
        drive(0.35, 27);
        sleep(1000);
        robot.shootLoop(2, 1500);
        drive(0.35, 14);
        rotate(40);
        resetOrientation();
        BaseAutonomous.DEBUG = true;
        drive(0.5, 20);
        BaseAutonomous.DEBUG = false;

        double beaconDistance = 46.5;
        if (isColor(Color.BLUE)){
            pushBeacon();
        } else {
            double buttonDis = 3.5;
            drive(0.3, buttonDis);
            sleep(1);
            pushBeacon();
            beaconDistance -= buttonDis;
        }
    }

    public boolean isColor(int color) {
        final int limit = 10;
        switch (color) {
            case Color.BLUE:
                RobotLog.ii("Info", robot.getBeaconSensor().blue() + "");
                return robot.getBeaconSensor().blue() > limit;
            case Color.RED:
                return robot.getBeaconSensor().red() > limit;
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
