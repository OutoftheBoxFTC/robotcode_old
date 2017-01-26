package org.ftc7244.robotcontroller.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by OOTB on 1/23/2017.
 */

public class Status {

    private static LinearOpMode autonomous;

    public static void setAutonomous(LinearOpMode autonomous) {
        Status.autonomous = autonomous;
    }

    public static boolean isStopRequested() {
        return (autonomous != null && autonomous.isStopRequested()) || Thread.interrupted();
    }
}
