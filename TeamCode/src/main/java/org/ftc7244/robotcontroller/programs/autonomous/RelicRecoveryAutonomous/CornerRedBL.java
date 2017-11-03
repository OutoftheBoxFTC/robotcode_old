package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name="CornerRedBL")
public class CornerRedBL extends RelicRecoveryPIDAutonamous {
    public void run(){
        waitForStart();
        robot.getSpring().setDirection(DcMotorSimple.Direction.FORWARD);
        sleep(1500);
        robot.drive(0.4, 0.4);
        sleep(600);
        robot.drive(0, 0);
        sleep(1500);
        robot.drive(0.4, -0.4);
        sleep(630);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.4, 0.4);
        sleep(900);
        robot.drive(0, 0);
        sleep(1500);
        robot.getIntake().setPower(1);
        sleep(5000);
        robot.getIntake().setPower(0);
        sleep(1500);
        robot.getSpring().setPower(1);
        sleep(500);
        robot.getSpring().setPower(0);
        while(opModeIsActive()){
            telemetry.addData("Done", true);
            telemetry.update();
        }
    }
}
