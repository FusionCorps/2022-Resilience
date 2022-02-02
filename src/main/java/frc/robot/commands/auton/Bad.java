package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Chassis;

public class Bad extends SequentialCommandGroup {

    Chassis mChassis;

    public Bad(Chassis chassis) {
        mChassis = chassis;
        addRequirements();

        addCommands();
    }

}
