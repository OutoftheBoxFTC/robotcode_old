package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.files.fileManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ftc72 on 6/18/2018.
 */
@TeleOp
public class fileIOTests extends OpMode {
    fileManager manager = new fileManager();
    byte[] buffer = new byte[128];
    @Override
    public void init() {
        try {
            manager.initialize(hardwareMap.appContext);
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getStackTrace());
        }
    }

    @Override
    public void loop() {
        try {
            manager.readFile(buffer);
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getStackTrace());
        }
        try {
            telemetry.addData("Test: ", new String(buffer, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            telemetry.addData("ERROR: ", e.getStackTrace());
        }
        telemetry.update();
    }
}
