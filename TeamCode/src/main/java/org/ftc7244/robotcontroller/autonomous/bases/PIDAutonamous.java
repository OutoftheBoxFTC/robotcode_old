package org.ftc7244.robotcontroller.autonomous.bases;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonomous.Status;


public abstract class PIDAutonamous extends LinearOpMode {

    private long end;
    @Override
    public void runOpMode() throws InterruptedException {
        Status.setAutonomous(this);
        startProviders();
        while (!isStarted()){
            whileNotStarted();
            idle();
        }
        boolean isError = false;
        try {
            beforeStart();
            end = System.currentTimeMillis() + 30000;
            run();
        }
        catch (Throwable t){
            String err = t.getMessage();
            //RobotLog.e(err);
            t.printStackTrace();
            isError = true;
        }
        finally {
            onEnd(isError);
            Status.setAutonomous(null);
        }
    }

    protected abstract void whileNotStarted();

    protected abstract void startProviders();

    public abstract void run() throws InterruptedException;

    protected abstract void onEnd(boolean err);

    protected abstract void beforeStart() throws InterruptedException;

    public long getAutonomousEnd() {
        return end;
    }
}
