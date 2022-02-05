package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.Constants.Shooter.SHOOTER_TARGET;

public class RunShooter extends CommandBase {

    Shooter mShooter;
    Indexer mIndexer;

    public RunShooter(Shooter shooter, Indexer indexer) {
        mShooter = shooter;
        mIndexer = indexer;
        addRequirements(mShooter, mIndexer);
    }

    @Override
    public void execute() {
        // TODO: Make this limelight dependent
        mShooter.setShooter(SHOOTER_TARGET);
        System.out.println(mShooter.isTarget());

        if (!mIndexer.break_beam.get() || mShooter.isTarget()) {
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
