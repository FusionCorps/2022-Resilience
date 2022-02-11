package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.math.SigmoidGenerator;
import frc.robot.subsystems.Chassis;

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

        angle = -(mChassis.ahrs.getAngle() % 360) + 180;



//        double axis0 = responseCurve.calculate(mController.getRawAxis(0));
//        double axis1 = responseCurve.calculate(mController.getRawAxis(1));
//        double axis4 = responseCurve.calculate(mController.getRawAxis(4));

        double axis0 = mController.getRawAxis(0);
        double axis1 = mController.getRawAxis(1);
        double axis4 = -mController.getRawAxis(4);

        if (axis0*axis0 + axis1*axis1 <= 0.0049) {
            axis0 = 0;
            axis1 = 0;
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

            if (abs(tx) <= 0.5) {
                tx = 0;
            }

            try {
                mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        0.0365*tx + (tx/(abs(tx)+0.02))*0.012);
            } catch (Exception e) {
            }

//            System.out.println("tx: " + tx);

        } else {
            try {
                mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                        strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                        rotLimiter.calculate(mController.getRawAxis(4)));
            } catch (Exception e) {
            }

        }



    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

