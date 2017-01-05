package org.ftc7244.robotcontrol.autonomous;

import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontrol.Westcoast;
import org.ftc7244.robotcontrol.core.PIDController;
import org.ftc7244.robotcontrol.sensor.GyroscopeProvider;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by OOTB on 10/16/2016.
 */
public abstract class BaseAutonomous extends LinearOpMode {

    public static boolean DEBUG = false;

    protected Westcoast robot;
    private GyroscopeProvider provider;

    public abstract void run() throws InterruptedException;

    @Override
    public void runOpMode() throws InterruptedException {
        this.robot = new Westcoast(this);
        this.provider = new GyroscopeProvider();
        robot.init();

        waitForStart();
        calibrate();

        try {
            resetOrientation();
            run();
        } catch (Throwable t) {
            RobotLog.i("Info", "Error");
            t.printStackTrace();
        } finally {
            provider.stop();
        }
    }

    public void drive(final double power, final double inches) throws InterruptedException {
        final double ticks = inches * EncoderBaseAutonomous.COUNTS_PER_INCH;
        EncoderBaseAutonomous.resetMotors(robot.getDriveLeft(), robot.getDriveRight());
        final int offset = getEncoderAverage();
        control(0, new Handler() {
            @Override
            public double offset() {
                return power;
            }

            @Override
            public boolean shouldTerminate() {
                return Math.abs(getEncoderAverage() - offset) >= ticks;
            }
        });
    }



    public void rotate(final double degrees) throws InterruptedException {
        final double target = degrees + provider.getZ();
        control(target, new Handler() {
            private long timestamp = -1;

            @Override
            public double offset() {
                return 0;
            }

            @Override
            public boolean shouldTerminate() {
                if (timestamp == -1 && Math.abs(provider.getZ() - target) < .5) timestamp = System.currentTimeMillis();
                else if (Math.abs(provider.getZ() - target) > .5) timestamp = -1;
                RobotLog.ii("STOP", provider.getZ() + ":" + target);
                return Math.abs(System.currentTimeMillis() - timestamp) > 100 && timestamp != -1;
            }
        });
        resetOrientation();
    }

    private void control(double degrees, Handler handler) throws InterruptedException {
        PIDController controller = new PIDController(-0.02, -0.00003, -3.25, 30);
        controller.setTarget(degrees);
        controller.setIntegralRange(15);
        controller.setDeadband(.25);

        do {
            double pid = controller.update(provider.getZ());
            logPID(controller);
            double offset = handler.offset();
            robot.getDriveLeft().setPower(offset + pid);
            robot.getDriveRight().setPower(offset - pid);
        } while (!handler.shouldTerminate() && !this.isStopRequested());

        robot.getDriveLeft().setPower(0);
        robot.getDriveRight().setPower(0);
    }

    public void resetOrientation() throws InterruptedException {
        provider.setZToZero();
        RobotLog.ii("Begin reset", "Reset");
        while (Math.abs(Math.round(provider.getZ())) > 1) {
            RobotLog.ii("hey", Math.abs(Math.round(provider.getZ())) + "");
            idle();
        }
    }

    private int getEncoderAverage() {
        return (robot.getDriveLeft().getCurrentPosition() + robot.getDriveRight().getCurrentPosition()) / 2;
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
