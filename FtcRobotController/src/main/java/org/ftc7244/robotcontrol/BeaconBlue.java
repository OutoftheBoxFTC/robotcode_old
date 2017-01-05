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
        drive(0.35, 29);
        sleep(1000);
        robot.shootLoop(2,  1500);
        drive(0.35, 11);
        //hi
        rotate(39.5);
        drive(0.5, 5);

        sleep(500);
        if (isColor(Color.BLUE)){
            pushBeacon();
        } else {
            drive(0.3, 5);
            sleep(1);
            pushBeacon();
        }

        rotate(90);
        robot.getIntake().setPower(1);
        drive(.75, 40);
    }

    public boolean isColor(int color) {
        RobotLog.ii("Color", robot.getBeaconSensor().blue() + ":" + robot.getBeaconSensor().red());
        switch (color) {
            case Color.BLUE:
                return robot.getBeaconSensor().blue() > robot.getBeaconSensor().red();
            case Color.RED:
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
