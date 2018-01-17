package org.ftc7244.robotcontroller.programs.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by BeaverDuck on 11/30/17.
 *
 * Used to train operator to respond to commands from coach in preparation for driver practice
 * Disabled because deemed unnecessary
 */
@TeleOp(name = "Training Program")
@Disabled
public class TrainingProgram extends OpMode{
    private HashMap<String, Button> inputs;
    private ArrayList<String> names;
    private ArrayList<Button> binds;
    private int currentIndex;

    @Override
    public void init() {
        inputs = new HashMap<>();
        names = new ArrayList<>();
        binds = new ArrayList<>();
        inputs.put("Raise Block", new Button(gamepad1, ButtonType.A));
        inputs.put("Lower Block", new Button(gamepad1, ButtonType.B));
        inputs.put("Raise Intake", new Button(gamepad1, ButtonType.D_PAD_UP));
        inputs.put("Lower Intake", new Button(gamepad1, ButtonType.D_PAD_DOWN));
        inputs.put("Intake Bottom", new Button(gamepad1, ButtonType.RIGHT_TRIGGER));
        inputs.put("Outtake Bottom", new Button(gamepad1, ButtonType.LEFT_TRIGGER));
        inputs.put("Intake Top", new Button(gamepad1, ButtonType.RIGHT_BUMPER));
        inputs.put("Outtake Top", new Button(gamepad1, ButtonType.LEFT_BUMPER));
        for(Map.Entry<String, Button> set : inputs.entrySet()){
            names.add(set.getKey());
            binds.add(set.getValue());
        }
        currentIndex = -1;
    }

    @Override
    public void loop() {
        if(currentIndex != -1){
            boolean pressed = false;
            while (!pressed){
                for (int i = 0; i < binds.size(); i++) {
                    if(binds.get(i).isPressed()){
                        i = binds.size();
                        pressed = true;
                        if(i==currentIndex)
                            telemetry.addData("Result", "Correct!");
                        else
                            telemetry.addData("Result", "WRONG");
                    }
                }
            }
        }
        currentIndex = new Random().nextInt(names.size());
        telemetry.addData("Command", names.get(currentIndex));
        telemetry.update();
    }
}
