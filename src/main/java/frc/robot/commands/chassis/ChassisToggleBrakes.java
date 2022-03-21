package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Chassis;

public class ChassisToggleBrakes extends InstantCommand {
    Chassis mChassis;

    public ChassisToggleBrakes(Chassis chassis) {
        mChassis = chassis;
    }

    @Override
    public void initialize() {
        mChassis.toggleBrakes();
    }

}
