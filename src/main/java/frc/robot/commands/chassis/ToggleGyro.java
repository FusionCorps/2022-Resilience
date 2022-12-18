package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Chassis;

public class ToggleGyro extends InstantCommand {

    // Toggles whether or not Gyro is used by RunFieldCentricSwerve.java

    Chassis mChassis;

    public ToggleGyro(Chassis chassis) {
        mChassis = chassis;
        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        mChassis.isUsingGyro = !mChassis.isUsingGyro;
    }

}
