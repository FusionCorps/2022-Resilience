package frc.robot;

public class Constants {

    public static int AXIS_FL_ID = 0;
    public static int AXIS_BL_ID = 1;
    public static int AXIS_FR_ID = 3;
    public static int AXIS_BR_ID = 7;

    public static int DRIVE_FL_ID = 8;
    public static int DRIVE_BL_ID = 9;
    public static int DRIVE_FR_ID = 4;
    public static int DRIVE_BR_ID = 5;

    public static int CODER_FL_ID = 12;
    public static int CODER_BL_ID = 2;
    public static int CODER_FR_ID = 13;
    public static int CODER_BR_ID = 11;

    public static double INDEXER_TARGET = 0.8;

    public static double TRACK_WIDTH_METERS = 0.7112;
    public static double TRACK_LENGTH_METERS = 0.7112;

    public static double SWERVE_FORWARD_SPEED_MAX = 4.2;
    public static double SWERVE_STRAFE_SPEED_MAX = 4.2;
    public static double SWERVE_ROT_SPEED_MAX = 6.0;

    public static double MAX_SPEED = 9.2;

    public static double AXIS_kF = 0.0;
    public static double AXIS_kP = 0.07;
    public static double AXIS_kI = 0.0;
    public static double AXIS_kD = 0.30;
    
    // note SDS default 0 0.2 0 0.1

    public static double DRIVE_kF = 0.0;
    public static double DRIVE_kP = 0.07;
    public static double DRIVE_kI = 0.0;
    public static double DRIVE_kD = 0.60;

    public static double STEERING_RATIO = 12.8;
    public static double DRIVING_RATIO = 6.86*6.86/6.75*1.25;

    public static double WHEEL_RADIUS_METERS = 0.0508;

    public static class Climb {
        public static int CLIMB_0_ID = 30;
        public static int CLIMB_1_ID = 31;

        public static int SERVO_L_ID = 0;
        public static int SERVO_R_ID = 1;

        public static double CLIMB_kF = 0.0;
        public static double CLIMB_kP = 0.07;
        public static double CLIMB_kI = 0.0;
        public static double CLIMB_kD = 0.30;

        public static double CLIMB_MIN_POS = -2048*100;
        public static double CLIMB_LOWER_POS = 0;
        public static double CLIMB_UPPER_POS = 204001-2048*15;
        public static double CLIMB_MAX_POS = 204001-2048*6.00;

    }

    public static class Indexer {
        public static int INDEXER_ID = 51;
        public static int IR_PORT = 0;
    }

    public static class Intake {
        public static int INTAKE_ID = 06;
    }

    public static class Shooter {
        public static int SHOOTER_ID = 20;

        public static double SHOOTER_TARGET = 0.55;

        public static double SHOOTER_LOWER_VEL = SHOOTER_TARGET*20000 - 500;
        public static double SHOOTER_UPPER_VEL = SHOOTER_TARGET*20000 + 500;

        public static double SHOOTER_kF = 0.04688;
        public static double SHOOTER_kP = 0.12;
        public static double SHOOTER_kI = 0.0;
        public static double SHOOTER_kD = 0.95;

        public static double SHOOTER_MAX_V = 21000;
        public static double SHOOTER_MIN_V = 500;

    }

}
