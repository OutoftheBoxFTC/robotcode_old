package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;


/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name = "Red Right")
public class RedRight extends PIDAutonomous {

    public void run() throws InterruptedException{
        robot.drive(.2, .2, 1400);
        sleep(1000);
        gyroscope.resetOrientation();
        sleep(1000);
        telemetry.addData("Gyro", gyroProvider.getZ());
        telemetry.addData("Image", imageTransformProvider.getImageRotation(ImageTransformProvider.RotationAxis.PITCH));
        telemetry.update();
        gyroscope.rotate(-107);
        gyroscope.drive(0.2, 30);
        robot.getSpring().setPosition(0.5);
        robot.getIntakeBottom().setPower(1);
        gyroscope.drive(-0.2, 10);
        robot.getIntakeBottom().setPower(0);


        gyroscope.drive(0.2, 6);
        gyroscope.rotate(90);
        gyroscope.rotate(90);
        robot.getJewelVerticle().setPosition(.15);


  //      robot.getSpring().setDirection(DcMotorSimple.Direction.REVERSE);
  //      robot.getSpring().setPower(1);
  //      sleep(500);
  //      robot.getSpring().setPower(0);
    }
}