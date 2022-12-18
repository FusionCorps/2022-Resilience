package frc.robot.commands.chassis;

import edu.wpi.first.math.filter.SlewRateLimiter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static frc.robot.RobotContainer.mController;


public class RunSwerveJoystick extends CommandBase {

    // original class
    // used for driving without FC

    // now is handled in a Toggle Command

    Chassis mChassis;

    public RunSwerveJoystick(Chassis chassis) {
        mChassis = chassis;
        this.addRequirements(mChassis);
    }


    @Override
    public void execute() {
        // pass args to swerve modules
        try {
            mChassis.runSwerve(mController.getRawAxis(1),
                    -mController.getRawAxis(0),
                    mController.getRawAxis(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Trying to drive");
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
