package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by FTC 7244 on 10/29/2017.
 */
@Autonomous(name = "Encoder Test")
public class EncoderTest extends PIDAutonomous{
    @Override
    public void run() throws InterruptedException {
        robot.drive(1, 1);
        while (!isStopRequested()){
            Logger.getInstance().queueData("AVERAGE", robot.getDriveEncoderAverage())
                    .queueData("Back Right", robot.getDriveBackRight().getCurrentPosition())
                    .queueData("Front Left", robot.getDriveFrontLeft().getCurrentPosition())
                    .queueData("Front Right", robot.getDriveFrontRight().getCurrentPosition());
        }
    }
}