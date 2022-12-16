package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Chassis;

public class ChassisToggleBrakes extends InstantCommand {
    Chassis mChassis;

    // simple command to toggle breaking mode on drive motors
    // We had some weird behavior when being defended at Ft. Worth, so we thought that this was the issue.
    // Most likely, our swerve alignment was just off.
    // command is now relegated to testing only.

    public ChassisToggleBrakes(Chassis chassis) {
        mChassis = chassis;
    }

    @Override
    public void initialize() {
        mChassis.toggleBrakes();
    }

}
