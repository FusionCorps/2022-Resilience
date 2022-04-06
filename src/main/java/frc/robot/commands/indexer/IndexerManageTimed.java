package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IndexerManageTimed extends CommandBase {

    Indexer mIndexer;

    Timer mTimer = new Timer();
    double mTime;

    public IndexerManageTimed(Indexer indexer, double time) {
        mIndexer = indexer;
        addRequirements(mIndexer);
        mTime = time;
    }

    @Override
    public void initialize() {
        mIndexer.configIndexer();
        mTimer.reset();
        mTimer.start();
    }

    @Override
    public void execute() {
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
        return mTimer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mIndexer.setIndexer(0.0);
    }



}
