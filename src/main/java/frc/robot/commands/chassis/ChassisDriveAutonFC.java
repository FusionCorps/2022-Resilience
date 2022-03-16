package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.StrictMath.PI;

public class ChassisDriveAutonFC extends CommandBase {

    // TODO: Make command that aligns wheels w/o movement overall

    Chassis mChassis;

    boolean reset;

    double mFwdSpeed;
    double mStrSpeed;
    double mRotSpeed;
    double mTime;

    public double angle = mChassis.ahrs.getAngle();

    private Timer timer = new Timer();

    public ChassisDriveAutonFC(Chassis chassis, double fwdSpeed, double strSpeed, double rotSpeed, double time) {

        mChassis = chassis;

        mFwdSpeed = fwdSpeed;
        mStrSpeed = strSpeed;
        mRotSpeed = rotSpeed;
        mTime = time;

        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();



        mChassis.solveAngles(-mFwdSpeed, mStrSpeed, mRotSpeed);


    }

    @Override
    public void execute() {

        angle = -(mChassis.ahrs.getAngle() % 360);

        double tx;

        double axis1 = -mFwdSpeed;
        double axis0 = mStrSpeed;
        double axis4 = mRotSpeed;

        if (mChassis.aiming) {
            tx = mChassis.limelightTable.getEntry("tx").getDouble(0.0);

            tx = tx + 29.4*(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI)));

            if (abs(tx) <= 0.8) {
                tx = 0;
            }

            double speed_K;

            if (mChassis.shooting) {
                speed_K = 0.00;
            } else {
                speed_K = 1.0;
            }



            try {
                mChassis.runSwerve(speed_K*(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        (axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        0.0135*tx + (tx/(abs(tx)+0.02))*0.006);
            } catch (Exception e) {
            }

//            System.out.println("tx: " + tx);

        } else {
            try {
                mChassis.runSwerve((axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        (axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        (axis4));
            } catch (Exception e) {
            }

        }

    }

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mChassis.runSwerve(0.0, 0.0, 0.0);
    }

}

