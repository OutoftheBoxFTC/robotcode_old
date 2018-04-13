package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.autonomous.terminators.ColorSensorTerminator;

/**
 * Created by ftc72 on 4/9/2018.
 */

@Autonomous(name = "Drive Color Red Test")
//@Disabled
public class DriveColorRedTest extends ControlSystemAutonomous {
    @Override
    public void run() {
        gyroscopePID.driveWithColorSensor(0.6, 50, robot.getDriveColor(), ColorSensorTerminator.Color.RED);
    }
}