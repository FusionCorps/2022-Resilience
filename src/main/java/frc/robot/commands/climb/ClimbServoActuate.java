package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class ClimbServoActuate extends InstantCommand {

    Climb mClimb;

    public ClimbServoActuate(Climb climb) {
        mClimb = climb;
        addRequirements(mClimb);
    }

    @Override
    public void execute() {
        mClimb.isClosed = !mClimb.isClosed;
    }



}
