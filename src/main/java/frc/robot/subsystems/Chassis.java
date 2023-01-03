package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.math.SwerveCalcs;
import frc.robot.modules.SwerveCombo;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static frc.robot.Constants.TRACK_LENGTH_METERS;
import static frc.robot.Constants.TRACK_WIDTH_METERS;
import static frc.robot.math.SwerveCalcs.*;
import static java.lang.Double.max;

public class Chassis extends SubsystemBase {

    // for auto recording, logging
    public FileWriter chassisWriter;

    // toggle variables
    public boolean aiming = false;
    public boolean shooting = false;

    public boolean isBraking = true;

    public boolean isUsingGyro = true;

    // LL NetworkTable
    public NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    // set up drive motors
    private static WPI_TalonFX drive0 = new WPI_TalonFX(Constants.DRIVE_FL_ID);
    private static WPI_TalonFX drive1 = new WPI_TalonFX(Constants.DRIVE_BL_ID);
    private static WPI_TalonFX drive2 = new WPI_TalonFX(Constants.DRIVE_FR_ID);
    private static WPI_TalonFX drive3 = new WPI_TalonFX(Constants.DRIVE_BR_ID);

    private static WPI_TalonFX axis0 = new WPI_TalonFX(Constants.AXIS_FL_ID);
    private static WPI_TalonFX axis1 = new WPI_TalonFX(Constants.AXIS_BL_ID);
    private static WPI_TalonFX axis2 = new WPI_TalonFX(Constants.AXIS_FR_ID);
    private static WPI_TalonFX axis3 = new WPI_TalonFX(Constants.AXIS_BR_ID);

    // CanCoders too
    CANCoder coder0 = new CANCoder(Constants.CODER_FL_ID); // FL
    CANCoder coder1 = new CANCoder(Constants.CODER_BL_ID); // BL
    CANCoder coder2 = new CANCoder(Constants.CODER_FR_ID); // FR
    CANCoder coder3 = new CANCoder(Constants.CODER_BR_ID); // BR

    // four swerve modules
    public SwerveCombo comboFL;
    public SwerveCombo comboBL;
    public SwerveCombo comboFR;
    public SwerveCombo comboBR;

    // gyro
    public static AHRS ahrs = new AHRS(SPI.Port.kMXP);

    // shoutout Ri3D Redux for reminding me I had to do this at some point
    private final Translation2d m_frontLeftLocation = new Translation2d(TRACK_WIDTH_METERS / 2.0, TRACK_LENGTH_METERS / 2.0);
    private final Translation2d m_frontRightLocation = new Translation2d(TRACK_WIDTH_METERS / 2.0, -TRACK_LENGTH_METERS / 2.0);
    private final Translation2d m_backLeftLocation = new Translation2d(-TRACK_WIDTH_METERS / 2.0, TRACK_LENGTH_METERS / 2.0);
    private final Translation2d m_backRightLocation = new Translation2d(-TRACK_WIDTH_METERS / 2.0, -TRACK_LENGTH_METERS / 2.0);

    private final SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
            m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation);

    private final SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
            m_kinematics,
            ahrs.getRotation2d(),
            new Pose2d(5, 5, ahrs.getRotation2d()));

    Field2d m_field;

    public Chassis() {

        // logging start
        // commented out normally
//        try {
//            chassisWriter = new FileWriter(new File("/home/lvuser/accelerations.csv"));
//            chassisWriter.append("accelX, accelY, accelZ, pitch, roll, yaw \n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        m_field = new Field2d();

        // gyro init
        // ahrs.calibrate();

        // config all the swerve modules
        drive0 = new WPI_TalonFX(Constants.DRIVE_FL_ID);
        drive1 = new WPI_TalonFX(Constants.DRIVE_BL_ID);
        drive2 = new WPI_TalonFX(Constants.DRIVE_FR_ID);
        drive3 = new WPI_TalonFX(Constants.DRIVE_BR_ID);

        axis0 = new WPI_TalonFX(Constants.AXIS_FL_ID);
        axis1 = new WPI_TalonFX(Constants.AXIS_BL_ID);
        axis2 = new WPI_TalonFX(Constants.AXIS_FR_ID);
        axis3 = new WPI_TalonFX(Constants.AXIS_BR_ID);

        comboFL = new SwerveCombo(axis0, drive0, coder0, 0);
        comboBL = new SwerveCombo(axis1, drive1, coder1, 1);
        comboFR = new SwerveCombo(axis2, drive2, coder2, 2);
        comboBR = new SwerveCombo(axis3, drive3, coder3, 3);

        SmartDashboard.putData("Field", m_field);
    }


    // pass gyro data for uploading
    public String getGyroData() {
        String retString = "";
        retString += ahrs.getRawAccelX() + ",";
        retString += ahrs.getRawAccelY() + ",";
        retString += ahrs.getRawAccelZ() + ",";
        retString += ahrs.getPitch() + ",";
        retString += ahrs.getRoll() + ",";
        retString += ahrs.getYaw() + ",";

        retString += ahrs.getDisplacementX() + ",";
        retString += ahrs.getDisplacementY() + ",";
        retString += ahrs.getDisplacementZ();

        return retString;

    }
    


    public void runSwerve(double fwd, double str, double rot_temp) {

        // convenience for negating
        double rot = rot_temp;
        // sometimes happens if we align the modules up wrong, easier to just fix in here than redo

        // get new values
        new SwerveCalcs(fwd, str, rot);

        // driving ratio to tweak or go "Turbo Mode"
        double ratio = 1.0;

        // assign new calc values
        double speedFL = 0;
        double speedBL = 0;
        double speedFR = 0;
        double speedBR = 0;

        try {
            speedFL = getSpeed(fwd, str, rot, 0);
            speedBL = getSpeed(fwd, str, rot, 1);
            speedFR = getSpeed(fwd, str, rot, 2);
            speedBR = getSpeed(fwd, str, rot, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // cap wheel speeds by finding max and adjusting all others down
        double maxWheelSpeed = max(max(speedFL, speedBL), max(speedFR, speedBR));

        if (maxWheelSpeed > Constants.MAX_SPEED) {
            ratio = (Constants.MAX_SPEED/ maxWheelSpeed);
        } else {
            ratio = 1.0;
        }

        // pass all values to motors
        this.comboFL.passArgs(ratio * speedFL, getAngle(fwd, str, rot, 0));
        this.comboBL.passArgs(ratio * speedBL, getAngle(fwd, str, rot, 1));
        this.comboFR.passArgs(ratio * speedFR, getAngle(fwd, str, rot, 2));
        this.comboBR.passArgs(ratio * speedBR, getAngle(fwd, str, rot, 3));

    }

    // does the same calcs but passes a velocity of 0
    // used to align the modules in auton
    public void solveAngles(double fwd, double str, double rot) {

        new SwerveCalcs(fwd, str, rot);

        double ratio = 1.0;

        double speedFL = 0;
        double speedBL = 0;
        double speedFR = 0;
        double speedBR = 0;

        try {
            speedFL = getSpeed(fwd, str, rot, 0);
            speedBL = getSpeed(fwd, str, rot, 1);
            speedFR = getSpeed(fwd, str, rot, 2);
            speedBR = getSpeed(fwd, str, rot, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }


        double maxWheelSpeed = max(max(speedFL, speedBL), max(speedFR, speedBR));

        if (maxWheelSpeed > Constants.MAX_SPEED) {
            ratio = (Constants.MAX_SPEED/ maxWheelSpeed);
        } else {
            ratio = 1.0;
        }


        this.comboFL.passArgs(0.03, getAngle(fwd, str, rot, 0));
        this.comboBL.passArgs(0.03, getAngle(fwd, str, rot, 1));
        this.comboFR.passArgs(0.03, getAngle(fwd, str, rot, 2));
        this.comboBR.passArgs(0.03, getAngle(fwd, str, rot, 3));



    }

    // get the data for logging
    public String getData() {
        String ret_string = "";

        ret_string += drive0.getMotorOutputPercent() + ",";
        ret_string += drive1.getMotorOutputPercent() + ",";
        ret_string += drive2.getMotorOutputPercent() + ",";
        ret_string += drive3.getMotorOutputPercent() + ",";
        ret_string += axis0.getMotorOutputPercent() + ",";
        ret_string += axis1.getMotorOutputPercent() + ",";
        ret_string += axis2.getMotorOutputPercent() + ",";
        ret_string += axis3.getMotorOutputPercent();



        return  ret_string;
    }

    // feed all the motors
    public void feedAll() {
        drive0.feed();
        drive1.feed();
        drive2.feed();
        drive3.feed();
        axis0.feed();
        axis1.feed();
        axis2.feed();
        axis3.feed();
    }

    // toggle Drive motor brakes
    public void toggleBrakes() {
        if (isBraking) {
            drive0.setNeutralMode(NeutralMode.Coast);
            drive1.setNeutralMode(NeutralMode.Coast);
            drive2.setNeutralMode(NeutralMode.Coast);
            drive3.setNeutralMode(NeutralMode.Coast);
            System.out.println("Coast");
        } else {
            drive0.setNeutralMode(NeutralMode.Brake);
            drive1.setNeutralMode(NeutralMode.Brake);
            drive2.setNeutralMode(NeutralMode.Brake);
            drive3.setNeutralMode(NeutralMode.Brake);
        }
        isBraking = !isBraking;
    }

    @Override
    public void periodic() {
        m_odometry.update(ahrs.getRotation2d(), comboFL.getState(), comboFR.getState(), comboBL.getState(),
                comboBR.getState());

        m_field.setRobotPose(m_odometry.getPoseMeters());
    }

    public void resetOdometry(Pose2d pose) {
        m_odometry.resetPosition(pose, ahrs.getRotation2d());
    }

    public Pose2d getPose() {
        return m_odometry.getPoseMeters();
    }

    // custom swerve is different than convention: speeds must be negated
    public void setModuleStates(SwerveModuleState[] desired) {
//        comboFL.passArgs(-1*desired[0].speedMetersPerSecond, desired[0].angle.getRadians());
//        comboFR.passArgs(-1*desired[1].speedMetersPerSecond, desired[1].angle.getRadians());
//        comboBL.passArgs(-1*desired[2].speedMetersPerSecond, desired[2].angle.getRadians());
//        comboBR.passArgs(-1*desired[3].speedMetersPerSecond, desired[3].angle.getRadians());
        comboFL.passState(desired[0]);
        comboFR.passState(desired[1]);
        comboBL.passState(desired[2]);
        comboBR.passState(desired[3]);
    }

    public Command followTrajectoryCommand(PathPlannerTrajectory traj, boolean isFirstPath) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    // Reset odometry for the first path you run during auto
                    if(isFirstPath){
                        this.resetOdometry(traj.getInitialHolonomicPose());
                    }
                }),
                new PPSwerveControllerCommand(
                        traj,
                        this::getPose, // Pose supplier
                        this.m_kinematics, // SwerveDriveKinematics
                        new PIDController(1, 0, 0), // X controller. Tune these values for your robot. Leaving them 0 will only use feedforwards.
                        new PIDController(1, 0, 0), // Y controller (usually the same values as X controller)
                        new PIDController(0, 0, 0), // Rotation controller. Tune these values for your robot. Leaving them 0 will only use feedforwards.
                        this::setModuleStates, // Module states consumer
                        this // Requires this drive subsystem
                )
        );
    }

}
