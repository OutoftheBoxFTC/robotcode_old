package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.ftc7244.robotcontroller.files.FileManager;

import java.io.IOException;

/**
 * Created by ftc72 on 6/18/2018.
 */
public class gamepad_PID_tuning extends OpMode {
    double p, i, d;
    String[] pid;
    byte[] buffer = new byte[64];
    FileManager fileManager;
    @Override
    public void init() {
        fileManager = new FileManager(hardwareMap.appContext);
        try {
            fileManager.initialize();
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getMessage());
            telemetry.update();
        }
        try {
            fileManager.readFile(buffer);
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getMessage());
            telemetry.update();
        }
        pid = (new String(buffer)).split(",");
        telemetry.addData("List", pid[1]);
        telemetry.update();
        //p = Double.valueOf(pid[0]);
        //i = Double.valueOf(pid[1]);
        //d = Double.valueOf(pid[2]);
    }

    @Override
    public void loop() {
        p += gamepad1.left_stick_y;
        i += gamepad1.right_stick_y;
        d += gamepad1.right_trigger - gamepad1.left_trigger;
        telemetry.addData("P I D", "P: " + p + " I: " + i + " D: " + d);
        telemetry.update();
        if(gamepad1.a){
            try {
                fileManager.writeFile(p, i, d);
            } catch (IOException e) {
                telemetry.addData("ERROR", e.getMessage());
                telemetry.update();
            }
        }
    }
}
