package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.XDrive;

/**
 * Created by Stargamer285 on 10/9/2017.
 */
@TeleOp(name="XDrive")
public class XDriveTeleop extends OpMode {
    private XDrive robot;
    @Override
    public void init(){
        robot = new XDrive(this);
        robot.init();
    }
    @Override
    public void loop(){
        
    }
}
