package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.files.FileManager;
import org.ftc7244.robotcontroller.network.NetworkManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by ftc72 on 6/18/2018.
 */
@TeleOp
public class fileIOTests extends OpMode {
    FileManager manager;
    NetworkManager networkManager;
    byte[] buffer = new byte[128];
    @Override
    public void init() {
        networkManager = new NetworkManager();
        try {
            manager = new FileManager(hardwareMap.appContext);
            manager.initialize();
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getMessage());
        } catch(NullPointerException e){

        }
        try {
            manager.startWriting();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            manager.writeFile(0.1, 0.2, 0.3);
        } catch (IOException e) {
            telemetry.addData("ERROR", e.getMessage());
        }
    }

    @Override
    public void loop() {
        try {
            networkManager.readSocket(buffer);
        } catch (IOException e) {
            telemetry.addData("ERRORA", e.getMessage());
        }
        try {
            telemetry.addData("Test", new String(buffer, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            telemetry.addData("ERROR: ", e.getMessage());
        }
        telemetry.update();
    }

    @Override
    public void stop(){
        try {
            manager.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}