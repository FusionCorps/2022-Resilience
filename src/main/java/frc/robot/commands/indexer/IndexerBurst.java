package frc.robot.commands.indexer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;

public class IndexerBurst extends CommandBase {
    // TODO: Make command that aligns wheels w/o movement overall

    Indexer mIndexer;


    double mTime;

    private Timer timer = new Timer();

    public IndexerBurst(Indexer indexer, double time) {

        mIndexer = indexer;


        mTime = time;

        addRequirements(mIndexer);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();


    }

    @Override
    public void execute() {

        mIndexer.setIndexer(-0.18);

    }

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mIndexer.setIndexer(0.0);

    }


}
