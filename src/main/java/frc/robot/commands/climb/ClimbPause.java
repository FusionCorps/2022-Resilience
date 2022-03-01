package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class ClimbPause extends InstantCommand {

    Climb mClimb;

    public ClimbPause(Climb climb) {
        mClimb = climb;
        addRequirements(mClimb);
    }

    @Override
    public void initialize() {
        mClimb.isPause = !mClimb.isPause;
    }

}
