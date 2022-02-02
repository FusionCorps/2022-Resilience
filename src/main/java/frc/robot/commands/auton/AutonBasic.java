package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.ChassisDriveAuton;
import frc.robot.commands.chassis.ChassisDriveAutonFC;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.subsystems.Chassis;

public class AutonBasic extends SequentialCommandGroup {

    Chassis mChassis;

    public AutonBasic(Chassis chassis) {


        mChassis = chassis;

        addRequirements(mChassis);

        addCommands(new ResetGyro(mChassis),
                new ChassisDriveAuton(mChassis, 0.01, 0.0, 0.0, 0.1),
                new ChassisDriveAuton(mChassis, 0.4, 0.0, 0.0, 0.4),
                new AutonTransition(mChassis, 0.4, 0.0, 0.0, 0.0, 0.4, 0.0, 0.5),
                new ChassisDriveAuton(mChassis, 0.0, 0.4, 0.0, 0.4)
        );

//        addCommands(new ResetGyro(mChassis),
//                new ChassisDriveAutonFC(mChassis, 0.05, 0.0, 0.0, 0.05),
//                new ChassisDriveAutonFC(mChassis, 0.4, 0.0, 0.0, 0.4),
//                new ChassisDriveAutonFC(mChassis, 0.1, 0.8, 0.6, 0.8));
    }

}
