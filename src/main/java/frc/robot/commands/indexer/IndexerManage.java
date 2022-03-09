package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

public class IndexerManage extends CommandBase {

    Indexer mIndexer;


    public IndexerManage(Indexer indexer) {
        mIndexer = indexer;
        addRequirements(mIndexer);
    }

    @Override
    public void initialize() {
        mIndexer.configIndexer();
    }

    @Override
    public void execute() {
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

}
