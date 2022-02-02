package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

public class IndexerManage extends CommandBase {

    Indexer mIndexer;
    Shooter mShooter;

    public IndexerManage(Indexer indexer, Shooter shooter) {
        mIndexer = indexer;
        mShooter = shooter;
        addRequirements(mIndexer, mShooter);
    }

    @Override
    public void execute() {
        if (!mIndexer.break_beam.get() || mShooter.isTarget()) {
            mIndexer.setIndexer(0.2);
        } else {
            mIndexer.setIndexer(0.0);
        }
    }

}
