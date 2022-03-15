package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.ChassisDriveAuton;
import frc.robot.commands.chassis.ChassisDriveAutonFC;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.commands.chassis.ToggleAim;
import frc.robot.commands.intake.RunIntakeTimed;
import frc.robot.commands.shooter.RunShooterVelocityTimed;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AutonAdvanced extends SequentialCommandGroup {

    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;

    public AutonAdvanced(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer);

        addCommands(new ResetGyro(mChassis),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, -0.4, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, -0.4, 0.0, 0.0, 0.5),
                        new RunIntakeTimed(mIntake, 0.5)),
                new AutonTransition(mChassis, -0.4, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                new ToggleAim(mChassis),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.5),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 3.0)
        );

    }


}
