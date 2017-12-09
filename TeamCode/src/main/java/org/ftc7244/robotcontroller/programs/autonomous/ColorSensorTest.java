package org.ftc7244.robotcontroller.programs.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.DataFilter;

/**
 * Created by BeaverDuck on 11/27/17.
 */
@Autonomous(name="Color Sensor Test")
public class ColorSensorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Westcoast robot = new Westcoast(this);
        robot.init();
        waitForStart();
        DataFilter filter = new DataFilter(10);
        while(isStarted()) {
            filter.update(robot.getJewelSensor().red());
            Logger.getInstance().queueData("Filtered", filter.getReading());
            Logger.getInstance().queueData("Unfiltered", robot.getJewelSensor().red());
        }
    }
}