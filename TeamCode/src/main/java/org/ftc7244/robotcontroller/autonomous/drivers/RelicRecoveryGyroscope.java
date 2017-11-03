package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.hardware.RelicRecoveryWestcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

/**
 * Created by FTC 7244 on 11/1/2017.
 */

public class RelicRecoveryGyroscope extends GyroscopeDrive {

    /**
     * Same as the parent constructor but passes a debug as fault by default since most users will
     * not want to debug the code.
     *
     * @param robot        access to motors on the robot
     * @param gyroProvider base way to read gyroscope values
     */
    public RelicRecoveryGyroscope(RelicRecoveryWestcoast robot, GyroscopeProvider gyroProvider) {
        super(robot, gyroProvider);
    }
}
