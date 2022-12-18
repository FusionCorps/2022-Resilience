package frc.robot.commands.shooter;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Shooter.SHOOTER_MAX_V;
import static frc.robot.Constants.Shooter.SHOOTER_MIN_V;
import static frc.robot.RobotContainer.mController;
import static java.lang.Math.*;
import static java.lang.Math.pow;
import static java.lang.StrictMath.PI;

public class RunShooterVelocity extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;
    Chassis mChassis;
    NetworkTable limelightTable;

    Timer mTimer = new Timer();

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);

    public RunShooterVelocity(Shooter shooter, Indexer indexer, Chassis chassis) {
        mShooter = shooter;
        mIndexer = indexer;
        mChassis = chassis;
        addRequirements(mShooter, mIndexer);
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void initialize() {
        mTimer.reset();
        mTimer.start();
    }

    @Override
    public void execute() {

        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);

        // linear approximation from LL values
        double v_calc = 1.018*1.035*(9248 - 107.4*ty);

        mShooter.target_velocity = mShooter.shootK*v_calc;

        // bound to prevent jams/overheating
        if (mShooter.target_velocity > SHOOTER_MAX_V) {
            mShooter.target_velocity = SHOOTER_MAX_V;
        } else if (mShooter.target_velocity < SHOOTER_MIN_V) {
            mShooter.target_velocity = SHOOTER_MIN_V;
        }

        // target thresholds
        mShooter.min_vel = mShooter.target_velocity - 120;
        mShooter.max_vel = mShooter.target_velocity + 120;



        // feed to PID controller
        mShooter.setShooterVelocity(mShooter.target_velocity);

        // pass to chassis
        if (mShooter.isShooting()) {
            mChassis.shooting = true;
        } else {
            mChassis.shooting = false;
        }

        // activate indexer based on shooter state
        if (mShooter.isTarget()) {
            mIndexer.setIndexer(-0.24);
            System.out.println(mShooter.target);
        // takes over indexer manage to load next while shooting
        } else {
            if (mIndexer.isAutomated) {
                if (mIndexer.break_beam.get()) {
                    mIndexer.setIndexer(-0.24);
                } else {
                    mIndexer.setIndexerVel(0);
                }
            } else {

                mIndexer.setIndexer(0.0);
            }
        }



    }

    // stop all motors
    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
        mIndexer.setIndexer(0.0);
        mChassis.shooting = false;
        mChassis.aiming = false;
    }

}
