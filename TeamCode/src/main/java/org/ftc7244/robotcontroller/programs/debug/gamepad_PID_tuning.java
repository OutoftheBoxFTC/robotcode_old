package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.ftc7244.robotcontroller.autonomous.ControlSystemAutonomous;
import org.ftc7244.robotcontroller.files.FileManager;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by ftc72 on 6/18/2018.
 */
@Autonomous
public class gamepad_PID_tuning extends ControlSystemAutonomous {
    public void run() {
        p += gamepad1.left_stick_y > 0.5 ? 0.0001 : gamepad1.left_stick_y < -0.5 ? -0.0001 : 0; //0.0035
        i += gamepad1.right_stick_y;
        d += gamepad1.right_trigger - gamepad1.left_trigger;
        telemetry.addData("P I D", "P: " + p + " I: " + i + " D: " + d);
        telemetry.update();
        if(gamepad1.a){
            try {
                fileManager.writeFile(p, i, d);
                updatePID(p, i, d);
                gyroscopePID.rotate(90);
            } catch (IOException e) {
                telemetry.addData("ERROR", e.getMessage());
                telemetry.update();
            }
        }
    }
}
