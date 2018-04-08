package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.hardware.Westcoast;

@Autonomous(name = "Vuforia Test")
public class VuforiaTest extends ControlSystemAutonomous {

    @Override
    public void run(){
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
