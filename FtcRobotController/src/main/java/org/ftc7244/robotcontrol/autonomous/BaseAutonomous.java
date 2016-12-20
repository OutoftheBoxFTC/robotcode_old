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

    public static final boolean DEBUG = true;
    private final static double COUNTS_PER_INCH = 1120 / (Math.PI * 3);

    private Westcoast robot;
    private GyroscopeProvider provider;

    public BaseAutonomous() {
        super();
        this.robot = new Westcoast(this);
        this.provider = new GyroscopeProvider();
    }


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();

        waitForStart();
        calibrate();

        drive(0);
        sleep(250);
        rotate(-90);
        sleep(250);
        drive(0);
        sleep(250);
        rotate(-90);
        sleep(250);
        drive(0);
        sleep(250);
        rotate(-90);
        sleep(250);

        provider.stop();
    }

    public void drive(final double distance) throws InterruptedException {
        final long counter = System.currentTimeMillis() + 1500;
        control(0, new Handler() {
            @Override
            public double offset() {
                return .5;
            }

            @Override
            public boolean shouldTerminate() {
                return counter < System.currentTimeMillis();
            }
        });
    }

    public void rotate(final double degrees) throws InterruptedException {
        control(degrees, new Handler() {
            private long timestamp = -1;

            @Override
            public double offset() {
                return 0;
            }

            @Override
            public boolean shouldTerminate() {
                if (timestamp == -1 && Math.abs(provider.getZ() - degrees) < .8) timestamp = System.currentTimeMillis();
                else if (Math.abs(provider.getZ() - degrees) > .8) timestamp = -1;

                return Math.abs(System.currentTimeMillis() - timestamp) > 250 && timestamp != -1;
            }
        });
    }

    private void control(double degrees, Handler handler) throws InterruptedException {
        PIDController controller = new PIDController(-0.02, -0.00003, -3.25, 30);
        controller.setTarget(degrees);
        controller.setIntegralRange(15);
        controller.setDeadband(.25);

        provider.setZToZero();

        do {
            double pid = controller.update(provider.getZ());
            double offset = handler.offset();
            robot.getDriveLeft().setPower(offset + pid);
            robot.getDriveRight().setPower(offset + -pid);
        } while (!handler.shouldTerminate());
    }

    private void logPID(PIDController controller) {
        if (!DEBUG) return;
        RobotLog.ii("PID", "|" + controller.getProportional() * controller.getkP() + "|" + controller.getIntegral() * controller.getkI() + "|" + controller.getDerivative() * controller.getkD() + "|" + provider.getZ());
    }

    private void calibrate() throws InterruptedException {
        provider.start((SensorManager) hardwareMap.appContext.getSystemService(SENSOR_SERVICE), 1);
        sleep(1000);
    }

    private interface Handler {
        double offset();

        boolean shouldTerminate();
    }
}
