package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.kauailabs.navx.ftc.AHRS;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;

/**
 * Created by BeaverDuck on 11/19/17.
 */

public interface NavxRobot {
    NavxMicroNavigationSensor getNavX();
}
