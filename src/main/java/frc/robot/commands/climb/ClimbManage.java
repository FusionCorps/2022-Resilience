package frc.robot.commands.climb;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.*;
import frc.robot.subsystems.Climb;

import static frc.robot.Constants.Climb.CLIMB_MAX_POS;
import static frc.robot.Constants.Climb.CLIMB_MIN_POS;
import static java.lang.Math.abs;


public class ClimbManage extends CommandBase {

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

        if (mClimb.isClosed) {
            mClimb.setServoAngles(52, 130);
        } else {
            mClimb.setServoAngles(115, 52);
        }



        if (mClimb.isPause) {
            mClimb.setClimb(0.0);
        } else {
            if (key == 0) {
                // TODO: is this how I should handle this
                mClimb.setClimb(0.1);
            } else if (key == 1) {
                mClimb.setClimb(0.95);
            } else if (key == 2) {
                mClimb.setClimb(0.95);
            } else if (key == 3) {
                mClimb.setClimb(0.95);
            } else {
                mClimb.setClimbPos(CLIMB_MAX_POS + 0.25*2048);
            }

        }


    }

}
