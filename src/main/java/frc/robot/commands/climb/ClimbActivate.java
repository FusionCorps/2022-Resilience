package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climb;


public class ClimbActivate extends CommandBase {

    // simple command to pull the climb arm down
    // would most likely benefit from some form of PID control

    Climb mClimb;

    public ClimbActivate(Climb climb) {
        mClimb = climb;
        addRequirements(mClimb);
    }

    @Override
    public void execute() {
        // check to make sure that climb is not below minimum allowed position
        int key = mClimb.getClimbPosKey();

        // force climb above minimum, else just pull climb down
        if (key == 0) {
            mClimb.setClimb(0.1);
        } else {
            mClimb.setClimb(-0.5);
        }

    }

    @Override
    public void end(boolean isFinished) {
        mClimb.setClimb(0.0);
        System.out.println("Climb Ended");
    }

}
