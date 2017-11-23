package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by BeaverDuck on 9/20/17.
 */
@Autonomous(name = "Vuforia Debug")
public class VuforiaDebug extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        //imageDrive.allignToImage();
        while (opModeIsActive()) ;
    }
}