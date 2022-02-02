package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.math.SwerveCalcs;
import frc.robot.modules.SwerveCombo;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static frc.robot.math.SwerveCalcs.*;
import static java.lang.Double.max;

public class Chassis extends SubsystemBase {

    public FileWriter chassisWriter;

    public boolean aiming = false;

    public NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    private static WPI_TalonFX drive0 = new WPI_TalonFX(Constants.DRIVE_FL_ID);
    private static WPI_TalonFX drive1 = new WPI_TalonFX(Constants.DRIVE_BL_ID);
    private static WPI_TalonFX drive2 = new WPI_TalonFX(Constants.DRIVE_FR_ID);
    private static WPI_TalonFX drive3 = new WPI_TalonFX(Constants.DRIVE_BR_ID);

    private static WPI_TalonFX axis0 = new WPI_TalonFX(Constants.AXIS_FL_ID);
    private static WPI_TalonFX axis1 = new WPI_TalonFX(Constants.AXIS_BL_ID);
    private static WPI_TalonFX axis2 = new WPI_TalonFX(Constants.AXIS_FR_ID);
    private static WPI_TalonFX axis3 = new WPI_TalonFX(Constants.AXIS_BR_ID);

    CANCoder coder0 = new CANCoder(Constants.CODER_FL_ID); // FL
    CANCoder coder1 = new CANCoder(Constants.CODER_BL_ID); // BL
    CANCoder coder2 = new CANCoder(Constants.CODER_FR_ID); // FR
    CANCoder coder3 = new CANCoder(Constants.CODER_BR_ID); // BR


    public SwerveCombo comboFL;
    public SwerveCombo comboBL;
    public SwerveCombo comboFR;
    public SwerveCombo comboBR;

    public static AHRS ahrs;



    public Chassis() {

        try {
            chassisWriter = new FileWriter(new File("/home/lvuser/accelerations.csv"));
            chassisWriter.append("accelX, accelY, accelZ, pitch, roll, yaw \n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ahrs = new AHRS(SPI.Port.kMXP);
        ahrs.calibrate();

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
    }



    public void resetHeading() {
        ahrs.reset();
    }

    public String getGyroData() {
        String retString = "";
        retString += ahrs.getRawAccelX() + ",";
        retString += ahrs.getRawAccelY() + ",";
        retString += ahrs.getRawAccelZ() + ",";
        retString += ahrs.getPitch() + ",";
        retString += ahrs.getRoll() + ",";
        retString += ahrs.getYaw();

        return retString;

    }
    


    public void runSwerve(double fwd, double str, double rot) {

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


        this.comboFL.passArgs(ratio * speedFL, getAngle(fwd, str, rot, 0));
        this.comboBL.passArgs(ratio * speedBL, getAngle(fwd, str, rot, 1));
        this.comboFR.passArgs(ratio * speedFR, getAngle(fwd, str, rot, 2));
        this.comboBR.passArgs(ratio * speedBR, getAngle(fwd, str, rot, 3));



    }

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


}
