package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "PID Autonamous Test")
public class CornerBlueBL extends PIDAutonomous {

    public void run() throws InterruptedException{
        gyroscope.drive(.15, 30);
        sleep(500);
        gyroProvider.calibrate();
        gyroscope.rotate(80);
  //      gyroscope.drive(0.5, 38.5);
  //      gyroscope.rotate(-30);
   //     robot.getIntakeBottom().setPower(1);
   //     sleep(2000);
  //      robot.getIntakeBottom().setPower(0);
  //      sleep(1500);
  //      robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
  //      robot.getSpring().setPower(1);
  //      sleep(500);
  //      robot.getSpring().setPower(0);
    }
}