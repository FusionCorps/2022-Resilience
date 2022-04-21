package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.*;
import frc.robot.commands.indexer.IndexerManageTimed;
import frc.robot.commands.intake.RunIntakeTimed;
import frc.robot.commands.shooter.RunShooterVelocityTimed;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AutonAdvancedPlusYoink extends SequentialCommandGroup {

    // Aggressively Hypothetical

    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;

    public AutonAdvancedPlusYoink(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer, mIntake);

        addCommands(
                new ResetGyro(mChassis),
                new RunIntakeTimed(mIntake, -0.75, 0.65),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.2, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, 0.2, 0.0, 0.0, 2.4),
                        new RunIntakeTimed(mIntake, 0.65, 2.4)),
                new ParallelCommandGroup(new AutonTransition(mChassis, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                        new RunIntakeTimed(mIntake, 0.65, 0.5),
                        new IndexerManageTimed(mIndexer, 0.5)),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, 0.6, 0.5),
                new AutonTransition(mChassis, 0.0, 0.0, 0.6, 0.0, 0.0, 0.0, 0.5),
                new ToggleAim(mChassis),
                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new RunIntakeTimed(mIntake, 0.0, 0.5),
                                new RunIntakeTimed(mIntake, 0.6, 0.5),
                                new RunIntakeTimed(mIntake, 0.0, 1.5)
                        ),
                        new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 2.5),
                        new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 2.5)
                ),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 3.0),
                new ChassisLookToAngle(mChassis, -280, 2.0),

                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.2, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, 0.2, 0.0, 0.0, 2.4),
                        new RunIntakeTimed(mIntake, 0.65, 2.4)),
                new ParallelCommandGroup(new AutonTransition(mChassis, 0.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                        new RunIntakeTimed(mIntake, 0.65, 0.5),
                        new IndexerManageTimed(mIndexer, 0.5))
        );

    }


}
