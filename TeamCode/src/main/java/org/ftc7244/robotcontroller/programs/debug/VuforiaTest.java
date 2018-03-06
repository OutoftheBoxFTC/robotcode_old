package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Created by ftc7244 on 2/19/2018.
 */
@Autonomous(name = "Vuforia Test")
@Disabled
public class VuforiaTest extends ControlSystemAutonomous {

    @Override
    public void run() throws InterruptedException {
        Westcoast robot = new Westcoast(this);
        robot.init();
        robot.initServos();
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Reading", imageProvider.getImageReading());
            telemetry.update();
        }
    }
}