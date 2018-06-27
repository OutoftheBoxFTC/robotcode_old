package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.files.FileManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ftc72 on 6/26/2018.
 */
@TeleOp
public class fileReadTests extends OpMode {
    FileManager manager;
    byte[] buffer = new byte[128];
    @Override
    public void init() {
        try {
            manager = new FileManager(hardwareMap.appContext);
            manager.initialize();
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getMessage());
        } catch(NullPointerException e){

        }
    }

    @Override
    public void loop() {
        try {
            manager.readFile(buffer);
        } catch (IOException e) {
            telemetry.addData("ERRORA", e.getMessage());
        }
        try {
            telemetry.addData("Test", new String(buffer, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            telemetry.addData("ERROR: ", e.getMessage());
        }
        telemetry.addData("Exists", manager.fileExists);
        telemetry.update();
    }
}
