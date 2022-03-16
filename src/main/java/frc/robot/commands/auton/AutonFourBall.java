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

public class AutonFourBall extends SequentialCommandGroup {
    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;

    public AutonFourBall(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer, mIntake);

        addCommands(new ResetGyro(mChassis),
                new RunIntakeTimed(mIntake, -0.75, 0.65),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.3, 0.0, 0.5),
                new AutonTransition(mChassis, 0.0, 0.3, 0.0, 0.3, 0.0, 0.0, 0.5),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.3, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, 0.3, 0.0, 0.0, 0.8),
                        new RunIntakeTimed(mIntake, 0.75, 0.85)),
                new AutonTransition(mChassis, 0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5, 0.4),
                new AutonTransition(mChassis, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.4),
                new ToggleAim(mChassis),
                new RunIntakeTimed(mIntake, 0.75, 0.35),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.65),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 2.0),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, -0.7, 0.4),
                new AutonTransition(mChassis, 0.0, 0.0, -0.7, 0.0, 0.0, 0.0, 0.4),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.8, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new ChassisDriveAuton(mChassis, 0.8, 0.0, 0.0, 0.75),
                        new RunIntakeTimed(mIntake, 0.75, 0.85)),
                new AutonTransition(mChassis, 0.8, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                new ParallelCommandGroup(new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5, 0.2),
                        new RunIntakeTimed(mIntake, 0.75, 0.2)),
                new ParallelCommandGroup(new AutonTransition(mChassis, 0.0, 0.0, -0.5, 0.0, 0.0, 0.0, 0.2),
                        new RunIntakeTimed(mIntake, 0.75, 0.2)),
                new RunIntakeTimed(mIntake, 0.75, 1.25),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, -0.3, 0.0, 0.0, 0.5),
                new ChassisDriveAuton(mChassis, -0.3, 0.0, 0.0, 1.6),
                new AutonTransition(mChassis, -0.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.5),
                new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.0, 0.0, 0.8, 0.45),
                new AutonTransition(mChassis, 0.0, 0.0, 0.8, 0.0, 0.0, 0.0, 0.45),

                new RunIntakeTimed(mIntake, 0.75, 0.35),
                new ToggleAim(mChassis),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 0.85),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 3.0)
        );

    }


}
