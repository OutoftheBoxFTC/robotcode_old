package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by FTC 7244 on 10/6/17.
 */
@Autonomous(name = "ServoTests")
public class ServoTests extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException{
        double position = 1;
        Servo servo = hardwareMap.servo.get("Servo");
        waitForStart();
        while (opModeIsActive()){
            servo.setPosition(position);
            telemetry.addData("Position: ", servo.getPosition());
            telemetry.update(); //100 RPM over 60 seconds is One rotation per 5/3 seconds
        }
    }
}
