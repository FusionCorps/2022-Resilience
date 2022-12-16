package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.StrictMath.PI;

public class ChassisDriveAutonFC extends CommandBase {

    // pass field-centric values to chassis over a timed period

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
        // timer start
        timer.reset();
        timer.start();

        // align wheels
        mChassis.solveAngles(-mFwdSpeed, mStrSpeed, mRotSpeed);

    }

    @Override
    public void execute() {

        // get gyro angle, correct for negative direction and cap from 0 to 360
        angle = -(mChassis.ahrs.getAngle() % 360);

        double tx;

        double axis1 = -mFwdSpeed;
        double axis0 = mStrSpeed;
        double axis4 = mRotSpeed;

        // if chassis is aiming, get limelight value and turn to target
        if (mChassis.aiming) {
            tx = mChassis.limelightTable.getEntry("tx").getDouble(0.0);

            // setting max error for aiming
            if (abs(tx) <= 0.6) {
                tx = 0;
            }

            // make sure that chassis is not moving during the shot
            double speed_K;

            if (mChassis.shooting) {
                speed_K = 0.00;
            } else {
                speed_K = 1.0;
            }


            // pass limelight to the rotation value, with a minimum value to overcome friction
            // speedK can be tweaked to maybe allow for shooting on the move, but is untested
            try {
                mChassis.runSwerve(speed_K*(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        (axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        0.0135*tx + (tx/(abs(tx)+0.02))*0.006);
            } catch (Exception e) {
                // if one of the swerve modules is configured incorrectly, prevents crashing
            }


        } else {
            // pass commands without feeding limelight data
            try {
                mChassis.runSwerve((axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        (axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        (axis4));
            } catch (Exception e) {
                // prevents crashing from incorrect swerve module number
            }

        }

    }

    // check if timer is complete
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

