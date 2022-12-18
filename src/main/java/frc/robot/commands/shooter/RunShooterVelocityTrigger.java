package frc.robot.commands.shooter;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Shooter.SHOOTER_MAX_V;
import static frc.robot.Constants.Shooter.SHOOTER_MIN_V;
import static frc.robot.RobotContainer.mController;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.PI;

public class RunShooterVelocityTrigger extends CommandBase {
    Shooter mShooter;
    Indexer mIndexer;
    Chassis mChassis;
    NetworkTable limelightTable;

    // Running the shooter but using the trigger instead

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);

    public RunShooterVelocityTrigger(Shooter shooter, Indexer indexer, Chassis chassis) {
        mShooter = shooter;
        mIndexer = indexer;
        mChassis = chassis;
        addRequirements(mShooter, mIndexer);
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {

        // increment decrement shooter speed from d-pad
        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);
        if (mController.getPOV() == 90) {
            mShooter.shootK += 0.001;
        } else if (mController.getPOV() == 270) {
            mShooter.shootK -= 0.001;
        }

        // pass to ShuffleBoard
        mShooter.shootKTab.setDouble(mShooter.shootK);

        // run shoot command on LeftTrigger
        if (mController.getLeftTriggerAxis() > 0.7) {


            double axis0 = mController.getRawAxis(0);
            double axis1 = mController.getRawAxis(1);
            double axis4 = -mController.getRawAxis(4);

            double angle = mChassis.ahrs.getAngle();

            double fwd = fwdLimiter.calculate(axis1 * cos(angle / 360 * (2 * PI)) + axis0 * sin(angle / 360 * (2 * PI)));
            double str = fwdLimiter.calculate(axis1 * sin(angle / 360 * (2 * PI)) - axis0 * cos(angle / 360 * (2 * PI)));
            double ty = limelightTable.getEntry("ty").getDouble(0.0);

            mShooter.shootK = mShooter.shootKTab.getDouble(1.0);

            // TODO: WRONG COMMAND
            // haha yeah this is deprecated
            double v_calc = 1*(9248 - 107.4*ty);

            mShooter.target_velocity = mShooter.shootK * v_calc;

            if (mShooter.target_velocity > SHOOTER_MAX_V) {
                mShooter.target_velocity = SHOOTER_MAX_V;
            } else if (mShooter.target_velocity < SHOOTER_MIN_V) {
                mShooter.target_velocity = SHOOTER_MIN_V;
            }

            mShooter.min_vel = mShooter.target_velocity - 120;
            mShooter.max_vel = mShooter.target_velocity + 120;


            mShooter.setShooterVelocity(mShooter.target_velocity);

            System.out.println(mShooter.getShooterVel() - mShooter.target_velocity);


            if (mShooter.isShooting()) {
                mChassis.shooting = true;
            } else {
                mChassis.shooting = false;
            }



            if (mShooter.isTarget()) {
                mIndexer.setIndexer(-0.17);
            } else {
                if (mIndexer.isAutomated) {
                    if (mIndexer.break_beam.get()) {
                        mIndexer.setIndexer(-0.17);
                    } else {
                        mIndexer.setIndexerVel(0);
                    }
                } else {

                    mIndexer.setIndexer(0.0);
                }
            }

        } else {
            System.out.println(limelightTable.getEntry("tx").getDouble(0.0));
            if (mChassis.aiming) {
                mShooter.setShooter(0.41);
            } else {
                mShooter.setShooter(0.0);
            }
            if (mController.getPOV() == 180) {
                mIndexer.setIndexer(0.2);
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

    }

    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
        mIndexer.setIndexer(0.0);
        mChassis.shooting = false;
        mChassis.aiming = false;
    }

}
