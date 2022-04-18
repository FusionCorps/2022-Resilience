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
import static java.lang.Math.*;
import static java.lang.StrictMath.PI;

public class SetShooterSpeedTimed extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;
    Chassis mChassis;
    NetworkTable limelightTable;
    double mTime;

    private Timer timer = new Timer();

    private SlewRateLimiter fwdLimiter = new SlewRateLimiter(2.5);

    public SetShooterSpeedTimed(Shooter shooter, Indexer indexer, double time) {
        mShooter = shooter;
        mIndexer = indexer;
        mTime = time;
        addRequirements(mShooter, mIndexer);
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        // TODO: Make this limelight dependent


        double angle = mChassis.ahrs.getAngle();

        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);


//            double temp = 0.48 - 0.0085*ty + 0.3*fwd;
        // double temp = 0.49 - 0.009*ty; if battery low

        // TODO: write code dependent on voltage instead


        // shoot k 0.95 above 1.00 below

//            double v_calc = 0.476 - 0.00837 * ty + 0.015 * abs(str);
//        double v_calc = 0.969*(10618 + -219*ty + 5.29*pow(ty,2) + 0.532*pow(ty,3));
//        double v_calc = 10000 - 250/2*ty;
        double v_calc = 1.018*1.08*(9248 - 107.4*ty);


        mShooter.target_velocity = mShooter.shootK*v_calc;

        if (mShooter.target_velocity > SHOOTER_MAX_V) {
            mShooter.target_velocity = SHOOTER_MAX_V;
        } else if (mShooter.target_velocity < SHOOTER_MIN_V) {
            mShooter.target_velocity = SHOOTER_MIN_V;
        }

        mShooter.min_vel = mShooter.target_velocity - 120;
        mShooter.max_vel = mShooter.target_velocity + 120;




        mShooter.setShooterVelocity(mShooter.target_velocity);

        System.out.println(mShooter.getShooterVel() - mShooter.target_velocity);



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

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
        mIndexer.setIndexer(0.0);
    }

}


