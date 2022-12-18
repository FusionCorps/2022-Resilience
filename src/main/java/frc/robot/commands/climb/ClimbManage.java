package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.*;
import frc.robot.subsystems.Climb;

import static frc.robot.Constants.Climb.CLIMB_MAX_POS;
import static frc.robot.Constants.Climb.CLIMB_MIN_POS;
import static java.lang.Math.abs;


public class ClimbManage extends CommandBase {

    // allows climb to spool out between attempts
    // when Paused, climb is put into brake mode. used to pull the robot up in a controlled manner

    Climb mClimb;

    public ClimbManage(Climb climb) {
        mClimb = climb;
        addRequirements(mClimb);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        int key;
        key = mClimb.getClimbPosKey();


        // brake if paused
        if (mClimb.isPause) {
            mClimb.setClimb(0.0);
        } else {
            // slow release to ensure that robot does not just drop onto the bar
            if (key == 0) {
                mClimb.setClimb(0.1);
            // originally these were all different but after testing we decided that it was just best
            // if our driver could just shoot the arms out as fast as possible
            } else if (key == 1) {
                mClimb.setClimb(0.95);
            } else if (key == 2) {
                mClimb.setClimb(0.95);
            } else if (key == 3) {
                mClimb.setClimb(0.95);
            // prevent spool wrap-around at the top of the climb
            } else {
                mClimb.setClimbPos(CLIMB_MAX_POS + 0.25*2048);
            }

        }


    }

}
