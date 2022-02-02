package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.math.SigmoidGenerator;
import frc.robot.subsystems.Chassis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.PI;

public class FieldCentricRecord extends CommandBase {
    Chassis mChassis;

    FileWriter cWriter;

    public FieldCentricRecord(Chassis chassis) {
        mChassis = chassis;
        this.addRequirements(mChassis);

        try {
            cWriter = new FileWriter(new File("/home/lvuser/recording.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(4.5);
    private SlewRateLimiter strLimiter = new SlewRateLimiter(4.5);
    private SlewRateLimiter rotLimiter = new SlewRateLimiter(4.5);

    private SigmoidGenerator responseCurve = new SigmoidGenerator(1.0);

    public double angle = mChassis.ahrs.getAngle();

    @Override
    public void initialize() {
        mChassis.comboFR.zero();
        mChassis.comboBR.zero();
        mChassis.comboFL.zero();
        mChassis.comboBL.zero();

        try {
            cWriter = new FileWriter("/home/lvuser/recording.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void execute() {

        mChassis.feedAll();

        angle = -(mChassis.ahrs.getAngle() % 360);



        double axis0 = responseCurve.calculate(mController.getRawAxis(0));
        double axis1 = responseCurve.calculate(mController.getRawAxis(1));
        double axis4 = responseCurve.calculate(mController.getRawAxis(4));



//        try {
//            mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
//                    strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
//                    rotLimiter.calculate(mController.getRawAxis(4)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            mChassis.runSwerve(fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI))),
                    strLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI))),
                    rotLimiter.calculate(mController.getRawAxis(4)));
        } catch (Exception e) {
        }
//        System.out.println("Trying to drive");

        try {
            cWriter.append(mChassis.getData() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean isFinished) {
        try {
            cWriter.flush();
            cWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Writer closed");
    }

}


