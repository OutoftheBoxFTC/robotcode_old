package org.ftc7244.robotcontroller.programs.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/1/17.
 */
@Autonomous(name = "Blue Right")
public class BlueRight extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
//        robot.getSpring().resetDeviceConfigurationForOpMode();
        robot.knockOverJewel(Color.BLUE);
        sleep(500);
        robot.drive(-0.3, 0.3);
        sleep(750);
        robot.drive(0.3, 0.3);
        sleep(1000);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(-0.3, 0.3);
        sleep(700);
        robot.drive(0, 0);
        sleep(1000);
        robot.drive(0.3, 0.3);
        sleep(700);
        robot.drive(0, 0);
        sleep(500);
//        robot.getIntakeBottomRight().setPower(1);
//        robot.getIntakeBottomLeft().setPower(1);
        robot.getSpring().setPosition(0.6);
        sleep(500);
//        robot.getIntakeBottomRight().setPower(0);
//        robot.getIntakeBottomLeft().setPower(0);
        sleep(1000);
        robot.drive(-0.3, -0.3);
        sleep(150);
        robot.drive(0, 0);
        sleep(500);
        robot.drive(-0.3, 0.3);
        sleep(300);
        robot.drive(0, 0);
    }
}
