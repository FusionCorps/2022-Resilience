package frc.robot.commands.shooter;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.Math.pow;
import static java.lang.StrictMath.PI;

public class RunShooterVelocity extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;
    Chassis mChassis;
    NetworkTable limelightTable;

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);

    public RunShooterVelocity(Shooter shooter, Indexer indexer, Chassis chassis) {
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
        double str = fwdLimiter.calculate(axis1*sin(angle/360*(2*PI)) - axis0*cos(angle/360*(2*PI)));
        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);


//            double temp = 0.48 - 0.0085*ty + 0.3*fwd;
        // double temp = 0.49 - 0.009*ty; if battery low

        // TODO: write code dependent on voltage instead


        // shoot k 0.95 above 1.00 below

//            double v_calc = 0.476 - 0.00837 * ty + 0.015 * abs(str);
        double v_calc = 21500 * (0.466 - 0.00403 * ty + 0.000331*pow(ty, 2) - 0.0000213*pow(ty, 3));

        mShooter.target_velocity = mShooter.shootK*v_calc;

        mShooter.min_vel = mShooter.target_velocity - 150;
        mShooter.max_vel = mShooter.target_velocity + 150;




        mShooter.setShooterVelocity(mShooter.target);



        if (mShooter.isShooting()) {
            mChassis.shooting = true;
        } else {
            mChassis.shooting = false;
        }

        if (mShooter.isTarget()) {
            mIndexer.setIndexer(-0.17);
            System.out.println(mShooter.target);
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
