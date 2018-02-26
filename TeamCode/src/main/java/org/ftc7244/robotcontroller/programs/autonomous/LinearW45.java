package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

@Autonomous(name = "Linear WE 45")
public class LinearW45 extends ControlSystemAutonomous {
    @Override
    public void run() throws InterruptedException {
        robot.getDriveBackLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveBackRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontLeft().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getDriveFrontRight().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.drive(1, -1, 2000);
    }
}
