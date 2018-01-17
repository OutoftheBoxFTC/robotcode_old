package org.ftc7244.robotcontroller.programs.autonomous;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.ViewDisplay;
import com.disnodeteam.dogecv.detectors.CryptoboxDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.PIDAutonomous;

/**
 * Created by Eeshwar Laptop on 1/16/2018.
 */
@Autonomous(name="DogeCVTest")
public class DogeCVTest extends PIDAutonomous{
    DogeCV dogeCV = new DogeCV();
    CryptoboxDetector detector = new CryptoboxDetector();
    @Override
    public void run() throws InterruptedException {
        detector.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        imageProvider.stop();
        sleep(500);
        while(opModeIsActive()){
            telemetry.addData("CryptoBox LEFT", detector.getCryptoBoxLeftPosition());
            telemetry.addData("CryptoBox RIGHT", detector.getCryptoBoxRightPosition());
            telemetry.addData("CryptoBox CENTER", detector.getCryptoBoxCenterPosition());
        }
    }
}
