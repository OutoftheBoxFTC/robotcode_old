package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;

/**
 * Created by Eeshwar Laptop on 11/3/2017.
 */
@Autonomous(name="SpringTest")
public class ServoSpringTest extends LinearOpMode{
    public void runOpMode(){
        RelicRecoveryWestcoast robot = new RelicRecoveryWestcoast(this);
        robot.init();
        waitForStart();
        robot.getSpring().setDirection(DcMotorSimple.Direction.FORWARD);
        telemetry.addData("Servo:", robot.getSpring().getDeviceName());
        telemetry.update();
        robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
        robot.getSpring().setPower(1);
        sleep(500);
    }
}
