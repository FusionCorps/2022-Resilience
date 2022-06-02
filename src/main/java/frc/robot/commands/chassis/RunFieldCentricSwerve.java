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

    Chassis mChassis;


    double tx = 0;
    double tx_prev = 0;

    public RunFieldCentricSwerve(Chassis chassis) {
        mChassis = chassis;

        this.addRequirements(mChassis);
    }

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);
    private SlewRateLimiter strLimiter = new SlewRateLimiter(2.5);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(4.5);

    private SigmoidGenerator responseCurve = new SigmoidGenerator(1.0);

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

        angle = -(mChassis.ahrs.getAngle() % 360);
//
        if (!mChassis.isUsingGyro) {
            angle = 0;
        }



//        double axis0 = responseCurve.calculate(mController.getRawAxis(0));
//        double axis1 = responseCurve.calculate(mController.getRawAxis(1));
//        double axis4 = responseCurve.calculate(mController.getRawAxis(4));

        double axis0 = 0.4*mController.getRawAxis(0);
        double axis1 = 0.4*mController.getRawAxis(1);
        double axis4 = mController.getRawAxis(4);

        if (axis0*axis0 + axis1*axis1 <= 0.0016) {
            axis0 = 0;
            axis1 = 0;
        }

        if (abs(axis4) <= 0.02) {
            axis4 = 0;
        }





//        try {
//            mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
//                    strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
//                    rotLimiter.calculate(mController.getRawAxis(4)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (mChassis.aiming) {
            tx = mChassis.limelightTable.getEntry("tx").getDouble(0.0);

//            tx = tx + 29.4*(strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))));

            if (abs(tx) <= 0.8) {
                tx = 0;
            }

            double speed_K;

            if (mChassis.shooting) {
                speed_K = 0.00;
            } else {
                speed_K = 1.0;
            }

            System.out.println("ts" + mChassis.limelightTable.getEntry("ts").getDouble(0.0));
            System.out.println("ts0" + mChassis.limelightTable.getEntry("ts0").getDouble(0.0));
            System.out.println("ts1" + mChassis.limelightTable.getEntry("ts1").getDouble(0.0));



            try {
                mChassis.runSwerve(speed_K*fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        0.0115*tx + (tx/(abs(tx)+0.02))*0.006);
            } catch (Exception e) {
            }

//            System.out.println("tx: " + tx);

        } else {
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

