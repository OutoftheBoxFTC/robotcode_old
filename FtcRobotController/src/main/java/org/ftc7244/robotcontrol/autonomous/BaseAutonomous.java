package org.ftc7244.robotcontrol.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;
import org.ftc7244.robotcontrol.core.PIDController;
import org.ftc7244.robotcontrol.sensor.GyroscopeProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by OOTB on 10/16/2016.
 */
@Autonomous
public class BaseAutonomous extends LinearOpMode {

    public static final int INTERVAL_PID = 1;

    private Westcoast robot;
    private PIDController controller;
    private GyroscopeProvider provider;

    public BaseAutonomous() {
        super();
        this.robot = new Westcoast(this);
        this.controller = new PIDController(INTERVAL_PID, -0.2, 0, -0.1);
        this.provider = new GyroscopeProvider();
    }


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();

        waitForStart();
        //Dont start till the play button is clicked
        rotate(90);
    }


    protected void rotate(double degs) throws InterruptedException {
        PIDController controller = new PIDController(-0.0049, -0.000008, 0, 30);
        provider.start((SensorManager) hardwareMap.appContext.getSystemService(SENSOR_SERVICE), 1);
        sleep(3000);
        provider.setZToZero();
        controller.setTarget(degs);
        long target = System.currentTimeMillis() + 5000;
        do {
            double pid = controller.update(provider.getZ());
            robot.getDriveLeft().setPower(pid);
            robot.getDriveRight().setPower(-pid);
            RobotLog.ii("PID", "|" + controller.getProportional() * controller.getkP() + "|" + controller.getIntegral() * controller.getkI() + "|" + controller.getDerivative() * controller.getkD() + "|" + provider.getZ());
        } while (target > System.currentTimeMillis());
        provider.stop();
    }
}
