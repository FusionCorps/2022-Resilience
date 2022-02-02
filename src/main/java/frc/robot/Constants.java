package frc.robot;

public class Constants {

    public static int AXIS_FL_ID = 1;
    public static int AXIS_BL_ID = 7;
    public static int AXIS_FR_ID = 2;
    public static int AXIS_BR_ID = 3;

    public static int DRIVE_FL_ID = 0;
    public static int DRIVE_BL_ID = 5;
    public static int DRIVE_FR_ID = 6;
    public static int DRIVE_BR_ID = 4;

    public static int CODER_FL_ID = 11;
    public static int CODER_BL_ID = 13;
    public static int CODER_FR_ID = 10;
    public static int CODER_BR_ID = 12;

    public static int INDEXER_ID = 20;
    public static double INDEXER_TARGET = 0.35;

    public static double TRACK_WIDTH_METERS = 0.3429;
    public static double TRACK_LENGTH_METERS = 0.3429;

    public static double SWERVE_FORWARD_SPEED_MAX = 4.8;
    public static double SWERVE_STRAFE_SPEED_MAX = 4.8;
    public static double SWERVE_ROT_SPEED_MAX = 6.0;

    public static double MAX_SPEED = 9.2;

    public static double AXIS_kF = 0.0;
    public static double AXIS_kP = 0.07;
    public static double AXIS_kI = 0.0;
    public static double AXIS_kD = 0.30;

    public static double DRIVE_kF = 0.0;
    public static double DRIVE_kP = 0.07;
    public static double DRIVE_kI = 0.0;
    public static double DRIVE_kD = 0.60;

    public static double STEERING_RATIO = 12.8;
    public static double DRIVING_RATIO = 6.86;

    public static double WHEEL_RADIUS_METERS = 0.0508;

    public static class Climb {
        public static int CLIMB_0_ID = 30;
        public static int CLIMB_1_ID = 31;

        public static double CLIMB_kF = 0.0;
        public static double CLIMB_kP = 0.07;
        public static double CLIMB_kI = 0.0;
        public static double CLIMB_kD = 0.30;

    }

    public static class Indexer {
        public static int INDEXER_ID;
        public static int IR_PORT;
    }

    public static class Shooter {
        public static int SHOOTER_ID;

        public static double SHOOTER_LOWER_VEL = 1750.0;
        public static double SHOOTER_UPPER_VEL = 2250.0;
    }

}
