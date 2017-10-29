package org.ftc7244.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.hardware.XDrive;
import org.ftc7244.robotcontroller.sensor.gyroscope.PhoneGyroscopeProvider;

/**
 * Created by Eeshwar Laptop on 10/23/2017.
 */

@Autonomous(name="2017 Test")
public class XDRIVEAutonomousTest extends LinearOpMode {
    private XDrive robot = new XDrive(this);
    private PhoneGyroscopeProvider gyro = new PhoneGyroscopeProvider();
    private double startRot = 0;
    private double sleepTimeD = 0;
    private long sleepTime  = 0;
    private double rotTarget = 0;
    public void runOpMode(){
        robot.init();
        gyro.calibrate();
        gyro.start(hardwareMap);
        startRot = gyro.getZ();
        waitForStart();
        robot.getDriveBottomRight().setPower(1);
        robot.getDriveTopLeft().setPower(-1);
        sleepTimeD = (long)3.83333 * robot.getSpeed();
        sleepTime = (long) sleepTimeD;
        try {
            Thread.sleep(sleepTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        rotTarget = startRot  - 90;
        telemetry.addData("Rot:", gyro.getZ());
        telemetry.addData("Target:", rotTarget);
        telemetry.update();
        robot.getDriveBottomRight().setPower(0);
        robot.getDriveTopLeft().setPower(0);
        robot.getDriveBottomRight().setPower(1);
        robot.getDriveTopLeft().setPower(1);
        while(gyro.getZ() <= rotTarget){}
        robot.getDriveBottomRight().setPower(0);
        robot.getDriveTopLeft().setPower(0);
    }
}