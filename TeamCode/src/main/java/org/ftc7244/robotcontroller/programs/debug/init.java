package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.ftc7244.robotcontroller.files.FileManager;

import java.io.IOException;

/**
 * Created by ftc72 on 6/21/2018.
 */
public class init extends OpMode {
    FileManager manager;
    @Override
    public void init() {
        manager = new FileManager(hardwareMap.appContext);
        try {
            manager.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loop() {
        try {
            manager.writeFile(.1, .2, .3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
