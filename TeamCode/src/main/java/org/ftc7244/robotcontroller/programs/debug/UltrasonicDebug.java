package org.ftc7244.robotcontroller.programs.debug;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonomous.bases.VelocityVortexPIDAutonomous;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FTC 7244 on 3/6/2017.
 */
@Autonomous(name = "Ultrasonic Debug")
public class UltrasonicDebug extends VelocityVortexPIDAutonomous {

    private List<Double> leadingHistory = new ArrayList<>();
    private List<Double> trailingHistory = new ArrayList<>();

    @Override
    public void run() throws InterruptedException {
        double oldLeading = 0, oldTrailing = 0;
        while (!isStopRequested() && (getAutonomousEnd() - System.currentTimeMillis()) > 15000) {
            double leading = robot.getLeadingUltrasonic().getUltrasonicLevel(), trailing = robot.getTrailingUltrasonic().getUltrasonicLevel();
            if (oldLeading != leading || trailing != oldTrailing) {
                oldLeading = leading;
                oldTrailing = trailing;

                trailingHistory.add(trailing);
                leadingHistory.add(leading);

                telemetry.addLine("Leading = " + leading + "in");
                telemetry.addLine("Trailing = " + trailing + "in");
                telemetry.addLine("Difference = " + (leading - trailing) + "in");
                telemetry.update();
            }
        }

        ArrayList<Double> combined = new ArrayList<>();
        combined.addAll(leadingHistory);
        combined.addAll(trailingHistory);

        telemetry.addLine("Avg Leading = " + mean(leadingHistory));
        telemetry.addLine("Avg Trailing = " + mean(trailingHistory));
        telemetry.addLine("Deviation = " + standardDeviation(combined));
        telemetry.addLine("Total Records = " + combined.size() / 2);
        telemetry.update();

        while (!isStopRequested()) idle();

    }

    public double standardDeviation(List<Double> values) {
        double totalDeviation = 0, mean = mean(values);
        for (Double x : values) totalDeviation += Math.pow(x - mean, 2);
        return Math.sqrt(totalDeviation / values.size());
    }

    public double mean(List<Double> values) {
        double total = 0;
        for (Double x : values) total += x;
        return total / values.size();
    }
}
