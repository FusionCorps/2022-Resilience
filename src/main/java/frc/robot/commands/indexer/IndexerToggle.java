package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Indexer;

public class IndexerToggle extends InstantCommand {

    Indexer mIndexer;

    // toggle on/off automated indexing
    public IndexerToggle(Indexer indexer) {
        mIndexer = indexer;
        addRequirements(mIndexer);
    }

    @Override
    public void initialize() {
        mIndexer.isAutomated = !mIndexer.isAutomated;
    }

}
