package org.ftc7244.robotcontroller.programs.debug;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;

/**
 * Created by ftc72 on 3/17/2018.
 */
@Autonomous(name = "Jewel Blue Test")
//@Disabled
public class JewelTestBlue extends ControlSystemAutonomous {
    @Override
    public void run(){
        robot.init();
        robot.initServos();
        waitForStart();
        robot.knockOverJewel(Color.BLUE);
    }
}
