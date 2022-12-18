package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Chassis;

public class ToggleAim  extends InstantCommand {

    // toggles a boolean in the Chassis subsystem
    // default command checks for that and toggle aiming on/off

    Chassis mChassis;

    public ToggleAim(Chassis chassis) {
        mChassis = chassis;
        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        mChassis.aiming = !mChassis.aiming;
    }

}
