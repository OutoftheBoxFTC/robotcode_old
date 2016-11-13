package org.ftc7244.robotcontrol;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.autonomous.EncoderBaseAutonomous;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by OOTB on 11/11/2016.
 */

@Deprecated
public class OldBeaconBlue extends EncoderBaseAutonomous {
    private ExecutorService service;

    public OldBeaconBlue() {
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void run() throws InterruptedException {
        service.execute(new Runnable() {
            @Override
            public void run() {
                int red = 0, blue = 0;
                while (true) {
                    if (red != robot.getBeaconSensor().red() || blue != robot.getBeaconSensor().blue()) {
                        blue = robot.getBeaconSensor().blue();
                        red = robot.getBeaconSensor().red();
                        RobotLog.ii("Color", "Red: " + red + " Blue: " + blue);
                        if (red > 3 || blue > 3) {
                            robot.getDriveLeft().setPower(0.01);
                            robot.getDriveRight().setPower(0.01);
                        }
                        if (red > 3) {
                            RobotLog.i("Arm", "OUT");
                            robot.getBeaconPusher().setPosition(0);
                            robot.getDriveLeft().setPower(0.0);
                            robot.getDriveRight().setPower(0.0);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            robot.getBeaconPusher().setPosition(1);
                            break;
                        }
                    }
                    if (isStopRequested()) break;
                }
            }
        });

        drive(.65, 50, 50);
    }
}
