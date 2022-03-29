package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.ChassisDriveAuton;
import frc.robot.commands.chassis.ChassisDriveAutonFC;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.commands.chassis.ToggleAim;
import frc.robot.commands.indexer.IndexerManageTimed;
import frc.robot.commands.intake.RunIntakeTimed;
import frc.robot.commands.shooter.RunShooterVelocityTimed;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AutonFourBallFC extends SequentialCommandGroup {
    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;

    public AutonFourBallFC(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer, mIntake);

        addCommands(new ResetGyro(mChassis),
                // drop intake
                new RunIntakeTimed(mIntake, -0.75, 0.35),

                // collect ball 2
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.4, 0.0, 0.6),
                new AutonTransition(mChassis, 0.0, 0.4, 0.0, 0.6, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, 0.6, 0.0, 0.0, 0.8),
                        new RunIntakeTimed(mIntake, 0.75, 0.8)
                        ),
                new AutonTransition(mChassis, 0.6, 0.0, 0.0, 0.0, 0.0, 0.0, 0.25),

                // turn to goal
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, 0.9, 0.3),
                new AutonTransition(mChassis, 0.0, 0.0, 0.9, 0.0, 0.0, 0.0, 0.3),

                // aim and shoot balls 1 2
                new ToggleAim(mChassis),

                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.65),
                new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new RunIntakeTimed(mIntake, 0.75, 0.5),
                            new RunIntakeTimed(mIntake, 0.0, 2.0)
                    ),
                    new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 2.5),
                    new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 2.5)
                ),

                // reset post aim
                new ChassisDriveAuton(mChassis, 0.0, 0.0, 0.0, 0.1),

                // rotate to new angle
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, -0.8, 0.4),
                new AutonTransition(mChassis, 0.0, 0.0, -0.8, 0.0, 0.0, 0.0, 0.4),

                // move towards the station
                new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.8, 0.2, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAutonFC(mChassis, 0.8, 0.2, 0.0, 0.82),
                        new RunIntakeTimed(mIntake, 0.75, 0.82)),
                new ParallelCommandGroup(
                new AutonTransitionFC(mChassis, 0.8, 0.2, 0.0, 0.0, 0.0, 0.0, 0.4),
                        new RunIntakeTimed(mIntake, 0.75, 0.4)),

                // wait for balls to intake
                new ParallelCommandGroup(new RunIntakeTimed(mIntake, 0.75, 0.45),
                        new IndexerManageTimed(mIndexer, 0.45)),

                // run it in reverse with balls 3 4
                new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, -1.2, 0.0, 0.0, 0.5),
                new ChassisDriveAutonFC(mChassis, -1.2, 0.0, 0.0, 0.65),
                new AutonTransitionFC(mChassis, -1.2, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),

                // turn to target
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.4),
                new AutonTransition(mChassis, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0, 0.4),

                // aim and shoot balls 3 4
                new RunIntakeTimed(mIntake, 0.75, 0.35),
                new ToggleAim(mChassis),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.85),
                new ParallelCommandGroup(
                        new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 3.0),
                        new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 3.0)
                )
        );

    }


}
