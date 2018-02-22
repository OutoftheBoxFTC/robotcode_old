package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by ftc72 on 2/21/2018.
 */

@Autonomous(name = "Alternate Control System")
public class AlternateControlSystem extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        robot.getDriveBackLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveBackRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveBackLeft().setPowerFloat();
        double target = -90, offset = gyroProvider.getZ();
        while (opModeIsActive()) {
            if(Math.abs(target-(gyroProvider.getZ()-offset))>60)
                robot.drive(.45, -.45);
            else {
                double power = .45*(Math.abs(target-(gyroProvider.getZ()-offset))/60);
                robot.drive(power, -power);
            }
        }
    }
}
