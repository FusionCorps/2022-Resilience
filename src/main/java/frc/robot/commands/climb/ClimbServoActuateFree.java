package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Climb;

public class ClimbServoActuateFree extends CommandBase {

    Climb mClimb;

    public ClimbServoActuateFree(Climb climb) {
        mClimb = climb;
//        addRequirements(mClimb);
    }

    @Override
    public void initialize() {
        if (mClimb.isClosed) {
            System.out.println("Opening");
            mClimb.setServoAngles(115, 52);
        } else {
            mClimb.setServoAngles(52, 130);
        }
    }

    @Override
    public void end(boolean interrupted) {
        System.out.println("done");
        mClimb.isClosed = !mClimb.isClosed;
        if (!mClimb.isClosed) {
            mClimb.setServoFree();
        }
    }


}
