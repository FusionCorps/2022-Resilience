package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.math.SigmoidGenerator;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.StrictMath.PI;

public class RunFieldCentricSwerve extends CommandBase {

    // command encapsulating main driving functionality and aiming functionality
    // in future, should be split into two commands

    Chassis mChassis;

    // LL init
    double tx = 0;

    public RunFieldCentricSwerve(Chassis chassis) {
        mChassis = chassis;

        this.addRequirements(mChassis);
    }

    // aggressive Limiters on fwd, str to prevent tippage
    // especially for demoing the robot

    // in the future, I need to make a separate demo branch
    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);
    private SlewRateLimiter strLimiter = new SlewRateLimiter(2.5);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(4.5);

    // unused responseCurve feature. Currently is just linear.
    private SigmoidGenerator responseCurve = new SigmoidGenerator(1.0);

    // angle get
    public double angle = mChassis.ahrs.getAngle();

    @Override
    public void initialize() {
        mChassis.comboFR.zero();
        mChassis.comboBR.zero();
        mChassis.comboFL.zero();
        mChassis.comboBL.zero();
    }


    @Override
    public void execute() {

        // arguably don't need to loop angle by 360
        angle = -(mChassis.ahrs.getAngle() % 360);

        // quick gyroscope toggle feature
        // to avoid switching defaultCommand mid-match
        if (!mChassis.isUsingGyro) {
            angle = 0;
        }

        double axis0 = mController.getRawAxis(0);
        double axis1 = mController.getRawAxis(1);
        double axis4 = mController.getRawAxis(4);

        // deadzone: get magnitude of left stick input
        if (axis0*axis0 + axis1*axis1 <= 0.0016) {
            axis0 = 0;
            axis1 = 0;
        }

        // rotation deadzone
        if (abs(axis4) <= 0.02) {
            axis4 = 0;
        }


        if (mChassis.aiming) {
            // aiming routine

            // get LL input
            tx = mChassis.limelightTable.getEntry("tx").getDouble(0.0);

            // max allowable error
            if (abs(tx) <= 0.8) {
                tx = 0;
            }

            // drive adjustment coeff for shooting
            double speed_K;

            // make sure chassis does not move when shooting
            if (mChassis.shooting) {
                speed_K = 0.00;
            } else {
                speed_K = 1.0;
            }

            // pass LL input to rotation, and shoot
            try {
                mChassis.runSwerve(speed_K*fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        0.0115*tx + (tx/(abs(tx)+0.02))*0.006);
            } catch (Exception e) {
            }


        } else {
            // normal drive, offset by gyro angle for FC
            try {
                mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        rotLimiter.calculate(axis4));
            } catch (Exception e) {
            }

        }



    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

