package frc.robot.math;

import frc.robot.Constants;

import static java.lang.Math.atan2;

public class SwerveCalcs {

    // Runs the math needed for the swerve

    // angle from center to wheel
    static double alpha;
    // distance from center to wheel
    static double distToWheel;

    // get values in m/s for wheels
    static double forward_speed;
    static double strafe_speed;
    static double rot_added_speed;


    // IMPORTANT: alpha calc only works for SQUARE BASES (I think)
    // needs more testing to be robust for rectangular bases
    public SwerveCalcs(double forward_input, double strafe_input, double rot_input) {
        new Constants();

        this.alpha = Math.atan(Constants.TRACK_LENGTH_METERS/Constants.TRACK_WIDTH_METERS);
        this.distToWheel = Math.sqrt((Math.pow(Constants.TRACK_LENGTH_METERS, 2)) +
                (Math.pow(Constants.TRACK_WIDTH_METERS, 2)));

        this.forward_speed = forward_input*Constants.SWERVE_FORWARD_SPEED_MAX;
        this.strafe_speed = strafe_input*Constants.SWERVE_STRAFE_SPEED_MAX;
        this.rot_added_speed = rot_input*distToWheel*Constants.SWERVE_ROT_SPEED_MAX;
    }


    // pass new inputs, update variables
    public static void updateVals(double forward_input, double strafe_input, double rot_input) {
        new Constants();

        forward_speed = forward_input*Constants.SWERVE_FORWARD_SPEED_MAX;
        strafe_speed = strafe_input*Constants.SWERVE_STRAFE_SPEED_MAX;
        rot_added_speed = rot_input*distToWheel*Constants.SWERVE_ROT_SPEED_MAX;
    }

    // return the speeds for a drive motor for a given input
    public static double getSpeed(double forward_input, double strafe_input, double rot_input, int position) throws Exception { // position 0 1 2 3 referring to FL BL FR BR

        double tempSpeed;

        // update internal - just to keep things neat
        updateVals(forward_input, strafe_input, rot_input);

        // calcs depend on which corner the module is in
        if (position == 0) {
            double tempSpeedFwd = forward_speed - rot_added_speed*Math.cos(alpha);
            double tempSpeedStr = strafe_speed - rot_added_speed*Math.sin(alpha);
            tempSpeed = Math.sqrt(Math.pow(tempSpeedFwd, 2) + Math.pow(tempSpeedStr, 2));
        } else if (position == 1) {
            double tempSpeedFwd = forward_speed - rot_added_speed*Math.sin(alpha);
            double tempSpeedStr = strafe_speed + rot_added_speed*Math.cos(alpha);
            tempSpeed = Math.sqrt(Math.pow(tempSpeedFwd, 2) + Math.pow(tempSpeedStr, 2));
        } else if (position == 2) {
            double tempSpeedFwd = forward_speed + rot_added_speed*Math.sin(alpha);
            double tempSpeedStr = strafe_speed - rot_added_speed*Math.cos(alpha);
            tempSpeed = Math.sqrt(Math.pow(tempSpeedFwd, 2) + Math.pow(tempSpeedStr, 2));
        } else if (position == 3) {
            double tempSpeedFwd = forward_speed + rot_added_speed*Math.cos(alpha);
            double tempSpeedStr = strafe_speed + rot_added_speed*Math.sin(alpha);
            tempSpeed = Math.sqrt(Math.pow(tempSpeedFwd, 2) + Math.pow(tempSpeedStr, 2));
        } else {
            // avoid having a swerve not in the specified cases
            throw new Exception("Not a valid module!");
        }

        // gives speed necessary for drive motor
        return tempSpeed;

    }

    // getting the angle for a module given a new input
    public static double getAngle(double forward_input, double strafe_input, double rot_input, int position) { // position 0 1 2 3 referring to FL BL FR BR

        // 0 - FL
        // 1 - BL
        // 2 - FR
        // 3 - BR

        // if failure, return 0
        double tempAngle = 0;

        // update values
        updateVals(forward_input, strafe_input, rot_input);

        // again, calculation is position dependent
        if (position == 0) {
            double tempSpeedFwd = forward_speed - rot_added_speed*Math.cos(alpha);
            double tempSpeedStr = strafe_speed - rot_added_speed*Math.sin(alpha);
            tempAngle = atan2(tempSpeedStr, tempSpeedFwd);
        } else if (position == 1) {
            double tempSpeedFwd = forward_speed - rot_added_speed*Math.sin(alpha);
            double tempSpeedStr = strafe_speed + rot_added_speed*Math.cos(alpha);
            tempAngle = atan2(tempSpeedStr, tempSpeedFwd);
        } else if (position == 2) {
            double tempSpeedFwd = forward_speed + rot_added_speed*Math.sin(alpha);
            double tempSpeedStr = strafe_speed - rot_added_speed*Math.cos(alpha);
            tempAngle = atan2(tempSpeedStr, tempSpeedFwd);
        } else if (position == 3) {
            double tempSpeedFwd = forward_speed + rot_added_speed*Math.cos(alpha);
            double tempSpeedStr = strafe_speed + rot_added_speed*Math.sin(alpha);
            tempAngle = atan2(tempSpeedStr, tempSpeedFwd);
        } else {
            System.out.println("Not a valid module!");
        }

        return tempAngle;

    }
}

