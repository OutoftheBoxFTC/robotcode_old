package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.hardware.Westcoast;

/**
 * Created by Eeshwar Laptop on 11/3/2017.
 */
@Autonomous(name = "SpringTest")
public class ServoSpringTest extends LinearOpMode {
    public void runOpMode() {
        Westcoast robot = new Westcoast(this);
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
