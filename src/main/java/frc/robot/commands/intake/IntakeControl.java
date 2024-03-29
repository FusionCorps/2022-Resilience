package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

import static frc.robot.RobotContainer.mController;

public class IntakeControl extends CommandBase {

    // running intake from Trigger Control

    private SlewRateLimiter indexLimit = new SlewRateLimiter(5.0);
    Intake mIntake;

    public IntakeControl(Intake intake) {
        mIntake = intake;
        addRequirements(mIntake);
    }

    @Override
    public void execute() {
        // extake from D-Pad
        if (mController.getPOV() == 180) {
            mIntake.runIntake(-0.6);
        } else {
            // scalar multiplier
            mIntake.runIntake(indexLimit.calculate(
                    (mController.getRightTriggerAxis())
                            * Constants.INDEXER_TARGET));
        }
    }

    @Override
    public void end(boolean interrupted) {
        mIntake.runIntake(0.0);
    }

}
