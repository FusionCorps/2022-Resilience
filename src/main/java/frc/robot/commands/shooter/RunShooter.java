package frc.robot.commands.shooter;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Shooter.SHOOTER_TARGET;

public class RunShooter extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;
    NetworkTable limelightTable;

    public RunShooter(Shooter shooter, Indexer indexer) {
        mShooter = shooter;
        mIndexer = indexer;
        addRequirements(mShooter, mIndexer);
        limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void execute() {
        // TODO: Make this limelight dependent

        double ty = limelightTable.getEntry("ty").getDouble(0.0);

        if (ty <= -1.0 ) {
            double temp = 0.5 - 0.01*ty;
            mShooter.target = (temp % 1);

            mShooter.min_vel = mShooter.target*20000 - 150;
            mShooter.max_vel = mShooter.target*20000 + 150;

            System.out.println("ty: " + ty);
        }

        mShooter.setShooter(mShooter.target);


        if (mShooter.isTarget()) {
            mIndexer.setIndexer(-0.2);
        } else {
            mIndexer.setIndexer(0.0);
        }



    }

    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
        mIndexer.setIndexer(0.0);
    }

}
