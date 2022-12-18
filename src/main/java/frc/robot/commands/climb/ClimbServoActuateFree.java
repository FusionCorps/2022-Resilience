package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class ClimbServoActuateFree extends CommandBase {

    Climb mClimb;

    public ClimbServoActuateFree(Climb climb) {
        mClimb = climb;
    }

    @Override
    public void initialize() {
        // sees current state and swap opposite
        if (mClimb.isClosed) {
            mClimb.setServoAngles(115, 52);
        } else {
            mClimb.setServoAngles(52, 130);
        }
    }

    @Override
    public void end(boolean interrupted) {
        // swap state
        mClimb.isClosed = !mClimb.isClosed;
        if (!mClimb.isClosed) {
            // avoids breaking the Servo motors on glancing collisions while open.
            mClimb.setServoFree();
        }
    }


}
