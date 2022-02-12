package frc.robot.commands.shooter;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Shooter.SHOOTER_TARGET;
import static frc.robot.RobotContainer.mController;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.PI;

public class RunShooter extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;
    Chassis mChassis;
    NetworkTable limelightTable;

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);

    public RunShooter(Shooter shooter, Indexer indexer, Chassis chassis) {
        mShooter = shooter;
        mIndexer = indexer;
        mChassis = chassis;
        addRequirements(mShooter, mIndexer);
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void execute() {
        // TODO: Make this limelight dependent

        double axis0 = mController.getRawAxis(0);
        double axis1 = mController.getRawAxis(1);
        double axis4 = -mController.getRawAxis(4);

         double angle = mChassis.ahrs.getAngle();

        double fwd = fwdLimiter.calculate(axis1*cos(angle/360*(2*PI)) + axis0*sin(angle/360*(2*PI)));
        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        if (ty <= 7.0 ) {
//            double temp = 0.48 - 0.0085*ty + 0.3*fwd;
            double temp = 0.49 - 0.009*ty;
            mShooter.target = (temp % 1);

            mShooter.min_vel = mShooter.target*20000 - 300;
            mShooter.max_vel = mShooter.target*20000 + 300;


        }

        mShooter.setShooter(mShooter.target);
        System.out.println(mShooter.isTarget());


        if (mShooter.isShooting()) {
            mChassis.shooting = true;
        } else {
            mChassis.shooting = false;
        }

        if (mShooter.isTarget()) {
            mIndexer.setIndexer(-0.3);
        } else {
            mIndexer.setIndexer(0.0);
        }



    }

    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
        mIndexer.setIndexer(0.0);
        mChassis.shooting = false;
    }

}
