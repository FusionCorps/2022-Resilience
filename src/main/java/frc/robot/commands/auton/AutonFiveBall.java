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
import pabeles.concurrency.ConcurrencyOps;

public class AutonFiveBall extends SequentialCommandGroup {

    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;-

    public AutonFiveBall(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer, mIntake);

        double t_init = 0.5;

        double t_first = 0.48*7/4;

        double t_sec = 0.48*9/4 - 0.1;

        addCommands(
            new ResetGyro(mChassis),
            new ParallelCommandGroup(
                    new ChassisWheelsAlign(mChassis, 1.0, 0.0, 0.0, t_init),
                    new RunIntakeTimed(mIntake, -0.75, t_init)
            ),

//            new ParallelCommandGroup(
//                new SequentialCommandGroup(
//                    new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.8, 0.0, 0.0, t_first),
//                    new AutonTransitionFC(mChassis, 0.8, 0.0, 0.0, 0.0, 0.0, 0.0, t_first)
//                ),
//                new RunIntakeTimed(mIntake, 0.7, 2*t_first)
//            ),

            new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.8/2, 0.0, 0.0, t_first),
                            new AutonTransitionFC(mChassis, 0.8/2, 0.0, 0.0, 0.0, 0.0, 0.0, t_first)
                    ),
                    new RunIntakeTimed(mIntake, 0.7, 2*t_first)
            ),

            new ParallelCommandGroup(
                new ChassisLookToAngle(mChassis, -180, 1.5),
                    new RunIntakeTimed(mIntake, 0.7, 1.5)
            ),

            new ToggleAim(mChassis),

            new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new RunIntakeTimed(mIntake, 0.75, 0.5),
                            new RunIntakeTimed(mIntake, 0.0, 2.0)
                    ),
                    new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 2.5),
                    new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 2.5)
            ),

            new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.0, 0.8, 0.0, t_sec),
                            new AutonTransitionFC(mChassis, 0.0, 0.8, 0.0, 0.0, 0.0, 0.0, t_sec),
                            new AutonTransition(mChassis, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, t_sec/2),
                            new AutonTransition(mChassis, 0.5, 0.0, 0.0, 0.0, 0.0, 0.0, t_sec/2)
                    ),
                    new RunIntakeTimed(mIntake, 0.7, 3*t_sec),
                    new IndexerManageTimed(mIndexer, 3*t_sec)
            ),

            new ParallelCommandGroup(
                    new ChassisLookToAngle(mChassis, -135, 1.5),
                    new RunIntakeTimed(mIntake, 0.7, 1.5)
            ),

        new ToggleAim(mChassis),

        new ParallelCommandGroup(
                new SequentialCommandGroup(
                        new RunIntakeTimed(mIntake, 0.75, 0.5),
                        new RunIntakeTimed(mIntake, 0.0, 2.0)
                ),
                new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 2.5),
                new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 2.5)
        )

        );


    }


}

