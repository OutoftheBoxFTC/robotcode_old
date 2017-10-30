package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Created by Eeshwar Laptop on 10/29/2017.
 */
@Autonomous(name="Autonomous")
public class RelicRecoveryAutonomous extends RelicRecoveryPIDAutonamous {
    public void run(){
        while(opModeIsActive()){
            waitForStart();
            robot.getDriveBackLeft().setPower(-1);
            robot.getDriveFrontLeft().setPower(-1);
            robot.getDriveBackLeft().setPower(1);
            robot.getDriveFrontLeft().setPower(1);
            telemetry.addLine("IMG: " + imageProvider.getImageReading());
            telemetry.update();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.getDriveBackLeft().setPower(0);
            robot.getDriveFrontLeft().setPower(0);
            robot.getDriveBackLeft().setPower(0);
            robot.getDriveFrontLeft().setPower(0);
            try {
                imageDrive.allignToImage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(imageProvider.getImageDistance(ImageTransformProvider.TranslationAxis.Z) >= 914.4){
                robot.getDriveBackLeft().setPower(1);
                robot.getDriveFrontLeft().setPower(1);
                robot.getDriveBackLeft().setPower(-1);
                robot.getDriveFrontLeft().setPower(-1);
            }
            robot.getDriveBackLeft().setPower(0);
            robot.getDriveFrontLeft().setPower(0);
            robot.getDriveBackLeft().setPower(0);
            robot.getDriveFrontLeft().setPower(0);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
