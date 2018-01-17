package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 12/29/17.
 */
@Autonomous(name = "Rotate -163")
public class Turn163 extends PIDAutonomous {

    @Override
    public void run() throws InterruptedException {
        gyroscope.rotate(-170);
    }
}
