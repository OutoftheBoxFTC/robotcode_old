package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.ftc7244.robotcontroller.Westcoast;

/**
 * Created by Stargamer285 on 10/6/17.
 */
@Autonomous(name = "ServoTests")
public class ServoTests extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        double position = 1;
        Westcoast robot = new Westcoast(this);
        CRServo servo = hardwareMap.crservo.get("Servo");
        waitForStart();
        while (opModeIsActive()){
            servo.setPower(1);
            telemetry.addData("Position: ", servo.getPower());
            telemetry.update(); //100 RPM over 60 seconds is One rotation per 5/3 seconds
        }
    }
}
