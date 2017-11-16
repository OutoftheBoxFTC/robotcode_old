package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;

/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name="CornerRedBL")
public class CornerRedBL extends LinearOpMode {
    RelicRecoveryWestcoast robot = new RelicRecoveryWestcoast(this);
    public void runOpMode(){
        robot.init();
        robot.getSpring().resetDeviceConfigurationForOpMode();
        waitForStart();
        robot.getSpring().setDirection(DcMotorSimple.Direction.FORWARD);
        sleep(1500);
        robot.drive(0.3, -0.3);
        sleep(600);
        robot.drive(0.3, 0.3);
        sleep(1000);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, -0.3);
        sleep(800);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, 0.3);
        sleep(350);
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
        robot.drive(-0.3, 0.3);
        sleep(300);
        robot.drive(0, 0);
        while(opModeIsActive()){
            //Loop to prevent crash
        }
    }
}
