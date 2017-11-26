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
        gyroscope.drive(0.5, 30);
        gyroscope.rotate(-155);
        gyroscope.drive(0.5, 38.5);
        gyroscope.rotate(-30);

    }
}
