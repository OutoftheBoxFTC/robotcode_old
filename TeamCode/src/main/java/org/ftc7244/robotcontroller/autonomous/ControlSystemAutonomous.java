package org.ftc7244.robotcontroller.autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.datalogger.Logger;
import org.ftc7244.robotcontroller.autonomous.drivers.PIDGyroscopeDrive;
import org.ftc7244.robotcontroller.hardware.Westcoast;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.ImageTransformProvider;

/**
 * Contains all the code for different drive types including ${@link PIDGyroscopeDrive}.
 * Not much happens here beyond the essentials for each control method. It also
 * automatically handles wait for startImageReading since most of the setup is completed and only driving
 * instructions are needed.
 */
public abstract class ControlSystemAutonomous extends LinearOpMode {

    private final static long AUTONOMOUS_DURATION = 30 * 1000;

    protected final GyroscopeProvider gyroProvider;

    protected final PIDGyroscopeDrive gyroscopePID;

    protected final ImageTransformProvider imageProvider;

    protected Westcoast robot;
    private long end;

    protected final SubTask
            RAISE_INTAKE_TO_HOME = new SubTask() {
        @Override
        public void iterate() {
            robot.liftIntakeProportional(Westcoast.INTAKE_HOME_POSITION);
        }

        @Override
        public void stop() {
            robot.getIntakeLift().setPower(Westcoast.INTAKE_REST_POWER);
        }
    },
            LOWER_INTAKE_TO_MIN = new SubTask() {
        @Override
        public void iterate() {
            robot.getIntakeLift().setPower(robot.getIntakeLift().getCurrentPosition()<=Westcoast.INTAKE_MIN_POSITION?Westcoast.INTAKE_REST_POWER:-1);
        }

        @Override
        public void stop() {
            robot.getIntakeLift().setPower(Westcoast.INTAKE_REST_POWER);
        }
    };

    /**
     * Loads hardware, pid drives, and sensor providers
     */
    protected ControlSystemAutonomous() {
        robot = new Westcoast(this);

        gyroProvider = new RevIMUGyroscopeProvider();
        imageProvider = new ImageTransformProvider();

        gyroscopePID = new PIDGyroscopeDrive(robot, gyroProvider);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Logger.init();
        robot.init();
        robot.initServos();

        robot.getDriveBackLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveBackRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontLeft().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getDriveFrontRight().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Status.setAutonomous(this);
        //Initializes robot and debug features

        try {
            gyroProvider.start(hardwareMap);
            imageProvider.start(hardwareMap);
            while (!isStarted()) {
                if (gyroProvider.isCalibrated()) {
                    telemetry.addLine("Gyroscope calibrated!");
                    telemetry.update();
                } else  {
                    telemetry.addLine("No Connection");
                    telemetry.update();
                }
                idle();
            }

            gyroscopePID.resetOrientation();
            end = System.currentTimeMillis() + AUTONOMOUS_DURATION;
            //Calibrates and starts providers
            run();
        } catch (Throwable t) {
            RobotLog.e(t.getMessage());
            t.printStackTrace();
            //Logs unexpected errors to prevent app crashing
        } finally {
            gyroProvider.stop();
            imageProvider.stop();
            Status.setAutonomous(null);
            //Stops all providers regardless of error
        }
    }

    /**
     * Similar to the sleep command in {@link #sleep(long)}, except runs a command iteratively until
     * sleep time is served
     *
     * @param sleepTime The time spent waiting
     * @param runWhileSleeping The command iteratively iterate while the robot sleeps
     */

    protected void sleepWhile(long sleepTime, SubTask runWhileSleeping){
        long lastTime = System.currentTimeMillis(),
        currentTime = lastTime;
        while (currentTime-lastTime < sleepTime && opModeIsActive()){
            runWhileSleeping.iterate();
            currentTime = System.currentTimeMillis();
        }
        runWhileSleeping.stop();
    }

    /**
     * Lowers the vertical jewel arm, reads the jewel color on the right, and moves
     * the horizontal jewel arm left or right depending on the given parameter
     *
     * @param color color jewel to be knocked off the pedestal
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    protected void knockOverJewel(int color) throws InterruptedException {
        //color we want to get rid of
        robot.getJewelVertical().setPosition(0.26);
        robot.getJewelHorizontal().setPosition(0.45);
        sleep(1700);
        if(color== Color.RED)
            robot.getJewelHorizontal().setPosition(robot.isColor(Color.RED) ? 0.33 : 0.56);
        else if(color==Color.BLUE)
            robot.getJewelHorizontal().setPosition(robot.isColor(Color.RED) ? 0.56 : 0.33);
        sleep(250);
        robot.getJewelHorizontal().setPosition(0.73);
        robot.getJewelVertical().setPosition(0.67);
        sleep(500);
    }

    protected void extendRelicArm(){
        robot.getRelicSpool().setPower(-1);
        while (robot.getRelicSpool().getCurrentPosition()<1600);
        robot.getRelicSpool().setPower(0);
    }

    public long getAutonomousEnd() {
        return end;
    }

    /**
     * The custom autonomous procedure
     * @throws InterruptedException if code fails to terminate on stop requested
     */
    public abstract void run() throws InterruptedException;

    /**
     * Class to embody tasks which iterate while the robot is in a period of sleep or control
     */
    public abstract class SubTask {
        public abstract void iterate();
        public abstract void stop();
    }
}