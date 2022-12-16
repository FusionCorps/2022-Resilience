package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.StrictMath.PI;

public class ChassisDriveAutonFCAndAngle extends CommandBase {

    // Pass FC parameters for x/y movement, use gyro to look at a specific angle.
    // Would have been used for 4 ball auton, but never tested in comp.

    Chassis mChassis;

    // limiter to avoid snapping to angle too aggressively
    SlewRateLimiter turnLimit;

    double mFwdSpeed;
    double mStrSpeed;
    double mAngle;
    double mTime;

    public double angle = mChassis.ahrs.getAngle();

    private Timer timer = new Timer();

    public ChassisDriveAutonFCAndAngle(Chassis chassis, double fwdSpeed, double strSpeed, double angle, double time) {

        mChassis = chassis;

        mFwdSpeed = fwdSpeed;
        mStrSpeed = strSpeed;
        mAngle = angle;
        mTime = time;

        turnLimit = new SlewRateLimiter(4.5);

        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        mChassis.solveAngles(-mFwdSpeed, mStrSpeed, 0);

    }

    @Override
    public void execute() {

        // two forms of aiming implemented; one from LL, one from gyro

        angle = -(mChassis.ahrs.getAngle() % 360);

        double tx;
        double tx_gyro;

        tx_gyro = -mChassis.ahrs.getAngle() + mAngle;

        // gyro max acceptable error
        if (abs(tx_gyro) <= 0.8) {
            tx_gyro = 0;
        }

        // capping gyro angle at +/- 30 in order to prevent erratic behavior
        if (tx_gyro > 30) {
            tx_gyro = 30;
        } else if (tx_gyro < -30) {
            tx_gyro = -30;
        }

        double axis1 = -mFwdSpeed;
        double axis0 = mStrSpeed;

        // passing parameters to chassis, depending on whether or not LL is being used.
        if (mChassis.aiming) {
            tx = mChassis.limelightTable.getEntry("tx").getDouble(0.0);


            if (abs(tx) <= 0.6) {
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

        } else {
            try {
                mChassis.runSwerve((axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        (axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        turnLimit.calculate(0.0135*tx_gyro + (tx_gyro/(abs(tx_gyro)+0.02))*0.006));
            } catch (Exception e) {
            }

        }

    }

    // timer check
    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    // stop robot
    @Override
    public void end(boolean interrupted) {
        mChassis.runSwerve(0.0, 0.0, 0.0);
    }

}

