package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;

/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name="CornerBlueBL")
public class CornerBlueBL extends RelicRecoveryPIDAutonamous {
    public void run(){
        double countsPerInch = 134.4 / (4 * Math.PI);
        waitForStart();
        robot.getSpring().setDirection(DcMotorSimple.Direction.FORWARD);
        sleep(1500);
        robot.drive(0.3, 0.3);
        sleep(850);
        robot.drive(0, 0);
        sleep(1500);
        robot.drive(-0.3, 0.3);
        sleep(1000);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, 0.3);
        sleep(1250);
        robot.drive(0, 0);
        sleep(1500);
        robot.getIntake().setPower(1);
        sleep(2000);
        robot.getIntake().setPower(0);
        sleep(1500);
        robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
        robot.getSpring().setPower(1);
        sleep(500);
        robot.getSpring().setPower(0);
        sleep(1000);
        robot.drive(-0.3, -0.3);
        sleep(150);
        robot.drive(0, 0);
        sleep(500);
        while(opModeIsActive()){
            telemetry.addData("Done", true);
            telemetry.update();
        }
    }
}
