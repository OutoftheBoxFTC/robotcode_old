package org.ftc7244.robotcontroller.programs.autonomous.RelicRecoveryAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonomous.bases.RelicRecoveryPIDAutonamous;
import org.ftc7244.robotcontroller.sensor.gyroscope.NavXGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 11/6/2017.
 */
@Autonomous(name="encoderTest")
public class encoderTest extends RelicRecoveryPIDAutonamous {
    public void run(){
        double countsPerInch = (134.4 / (4 * Math.PI)) * (1.765 / 4);
/*        robot.getDriveFrontRight().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getDriveFrontRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        waitForStart();
*/
        waitForStart();
        int encoderCountInt = (int) Math.round(36 * countsPerInch);
        telemetry.addData("encoderCount:", encoderCountInt);
        telemetry.addData("difference from estimation:", 1123 - encoderCountInt);
        telemetry.update();
        //robot.resetEncoders();
        robot.getDriveFrontLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveBackLeft().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveFrontRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveBackRight().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.getDriveBackRight().setPower(0.4);
        robot.getDriveBackLeft().setPower(0.4);
        robot.getDriveFrontRight().setPower(0.4);
        robot.getDriveFrontLeft().setPower(0.4);
        while(opModeIsActive()){
            robot.getDriveBackRight().setTargetPosition(1120);
            robot.getDriveBackLeft().setTargetPosition(1120);
            robot.getDriveFrontRight().setTargetPosition(-1120);
            robot.getDriveFrontLeft().setTargetPosition(-1120);
            telemetry.addData("encoderCount:", robot.getDriveFrontRight().getCurrentPosition());
            telemetry.update();
        }
    }
}
