package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.datalogger.file.FileInterface;
import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by BeaverDuck on 12/6/17.
 */
@Autonomous(name = "File Writer Test")
public class FileWriterTest extends PIDAutonomous {
    @Override
    public void run() throws InterruptedException {
        Logger.getInstance().queueData("Output", FileInterface.readLines("Test.txt")==null?1:0);
        FileInterface.writeToFile("Test.txt", false, new ArrayList<>(Arrays.asList("Test")));
    }
}
