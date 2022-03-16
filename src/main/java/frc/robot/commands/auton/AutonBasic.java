package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.ChassisDriveAuton;
import frc.robot.commands.chassis.ChassisDriveAutonFC;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.commands.chassis.ToggleAim;
import frc.robot.commands.shooter.RunShooterVelocity;
import frc.robot.commands.shooter.RunShooterVelocityTimed;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

public class AutonBasic extends SequentialCommandGroup {

    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;

    public AutonBasic(Chassis chassis, Shooter shooter, Indexer indexer) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;

        addRequirements(mChassis, mShooter, mIndexer);

        addCommands(new ResetGyro(mChassis),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, -0.4, 0.0, 0.0, 0.5),
                new ChassisDriveAuton(mChassis, -0.4, 0.0, 0.0, 0.75),
                new AutonTransition(mChassis, -0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                new ToggleAim(mChassis),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.5),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 3.5)
        );

    }

}
