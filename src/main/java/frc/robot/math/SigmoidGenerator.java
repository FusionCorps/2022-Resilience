package frc.robot.math;

import static java.lang.Math.exp;

public class SigmoidGenerator {

    // Class to handle a response curve
    // ended up not using, but could be fun to play around with

    double k;

    public SigmoidGenerator(double growthRate) {
        k = growthRate;
    }

    public double calculate(double input) {

        double output;

        if (input >= 0.0) {
            output = 1 / (1 + exp(-k * 12 * (input - 0.5)));
        } else {
            output = -1 / (1 + exp(k * 12 * (input + 0.5)));
        }
        return output;
    }

}
