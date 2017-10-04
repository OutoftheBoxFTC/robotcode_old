package org.ftc7244.robotcontroller.autonomous.drivers;

import org.ftc7244.robotcontroller.Westcoast;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDController;
import org.ftc7244.robotcontroller.autonomous.controllers.PIDDriveControl;

/**
 * Created by BeaverDuck on 10/4/17.
 */

public class ImageDistanceDrive extends PIDDriveControl{

    public ImageDistanceDrive(PIDController controller, Westcoast robot) {
        super(controller, robot);
    }

    @Override
    public double getReading() {
        return 0;
    }
}