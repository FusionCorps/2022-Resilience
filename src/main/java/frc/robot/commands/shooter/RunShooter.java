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
import static java.lang.Math.*;
import static java.lang.StrictMath.PI;

public class RunShooter extends CommandBase {

    // old shooter default command

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

        // polling controller for shoot on the move, never really worked
        double axis0 = mController.getRawAxis(0);
        double axis1 = mController.getRawAxis(1);

        double angle = mChassis.ahrs.getAngle();

        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);

            // cubic curve of best fit
            // ultimately too pressure dependent to be good for competition
            double v_calc = 0.466 - 0.00403 * ty + 0.000331*pow(ty, 2) - 0.0000213*pow(ty, 3);

            mShooter.target = mShooter.shootK*v_calc;

            mShooter.min_vel = mShooter.target*21500 - 150;
            mShooter.max_vel = mShooter.target*21500 + 150;




        mShooter.setShooter(mShooter.target);


        // pass var to chassis
        // redundant, should just make an accessor function
        if (mShooter.isShooting()) {
            mChassis.shooting = true;
        } else {
            mChassis.shooting = false;
        }

        // spin up indexer when ready to fire
        if (mShooter.isTarget()) {
            mIndexer.setIndexer(-0.17);
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
