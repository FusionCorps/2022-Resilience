package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

import static frc.robot.RobotContainer.mController;

public class IndexerManage extends CommandBase {

    // general indexer command
    // run indexer until break beam is tripped
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

        // run indexer backwards to clear jams
        if (mController.getPOV() == 180) {
            mIndexer.setIndexer(0.2);
        } else {

            // index based on break beam
            // might have needed to use setIndexerVel to help both shots go to the same place
            if (mIndexer.isAutomated) {
                if (mIndexer.break_beam.get()) {
                    mIndexer.setIndexer(-0.22);
                } else {
                    mIndexer.setIndexerVel(0);
                }
            } else {
                mIndexer.setIndexer(0.0);
            }
        }
    }

}
