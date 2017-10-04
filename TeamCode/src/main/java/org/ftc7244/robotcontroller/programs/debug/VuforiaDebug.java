package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 9/20/17.
 */
@Autonomous(name = "Vuforia Debug")
public class VuforiaDebug extends LinearOpMode {
    Westcoast robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Westcoast(this);
        robot.init();
        robot.initVuforia(hardwareMap);
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Distance", robot.inchesFromPictograph(Westcoast.PosAxis.Z));
            telemetry.addData("Image Seen", robot.getPictographReading());
            telemetry.update();
        }
    }
}