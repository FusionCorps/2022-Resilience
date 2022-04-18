package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.chassis.*;
import frc.robot.commands.indexer.IndexerManageTimed;
import frc.robot.commands.intake.RunIntakeTimed;
import frc.robot.commands.shooter.RunShooterVelocityTimed;
import frc.robot.commands.shooter.SetShooterSpeedTimed;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class AutonFiveBall extends SequentialCommandGroup {


    Chassis mChassis;
    Shooter mShooter;
    Indexer mIndexer;
    Intake mIntake;

    public AutonFiveBall(Chassis chassis, Shooter shooter, Indexer indexer, Intake intake) {


        mChassis = chassis;
        mShooter = shooter;
        mIndexer = indexer;
        mIntake = intake;

        addRequirements(mChassis, mShooter, mIndexer, mIntake);

        double t_init = 0.3;

        double t_first = 0.48*7/6;

        double t_sec = 0.48*9/4;

        double t_third = 0.5;

        double t_fourth = 1.3;


        double t_fifth = 0.4;

        double t_sixth = 0.8;

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
                                new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.7, 0.0, 0.0, t_first),
                                new AutonTransitionFC(mChassis, 0.7, 0.0, 0.0, 0.0, 0.0, 0.0, t_first)
                        ),
                        new RunIntakeTimed(mIntake, 0.7, 2*t_first)
                ),

                new ParallelCommandGroup(
                        new ChassisLookToAngle(mChassis, -180, 1.5),
                        new RunIntakeTimed(mIntake, 0.7, 1.5),
                        new SetShooterSpeedTimed(mShooter, mIndexer, 1.5)
                ),

                new ToggleAim(mChassis),

                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new RunIntakeTimed(mIntake, 0.75, 0.5),
                                new RunIntakeTimed(mIntake, 0.0, 1.2)
                        ),
                        new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 1.7),
                        new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 1.7)
                ),

                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.0, 0.75, 0.0, t_sec-0.05),
                                new AutonTransitionFC(mChassis, 0.0, 0.75, 0.0, -0.45, 0.0, 0.0, t_sec),
                                new AutonTransitionFC(mChassis, -0.45, 0.0, 0.0, 0.0, 0.0, 0.0, t_sec/2)
                        ),
                        new RunIntakeTimed(mIntake, 0.7, 2.5*t_sec - 0.05),
                        new IndexerManageTimed(mIndexer, 2.5*t_sec - 0.05)
                ),

                new ParallelCommandGroup(
                        new ChassisLookToAngle(mChassis, -135, 0.4),
                        new RunIntakeTimed(mIntake, 0.7, 0.4),
                        new SetShooterSpeedTimed(mShooter, mIndexer, 0.4)
                ),

                new ToggleAim(mChassis),

                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new RunIntakeTimed(mIntake, 0.75, 0.5),
                                new RunIntakeTimed(mIntake, 0.0, 0.9)
                        ),
                        new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 1.4),
                        new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 1.4)
                ),

                new ParallelCommandGroup(
                    new SequentialCommandGroup(
                            new AutonTransitionFC(mChassis, 0.0, 0.0, 0.0, 0.08, 0.8, 0.5, t_third),
                            new ChassisDriveAutonFCAndAngle(mChassis,0.1, 0.8, 60, t_fourth),
                            new AutonTransitionFC(mChassis, 0.1, 0.8, 0.0, 0.0, 0.0, 0.0, t_third)
                    ),
                        new RunIntakeTimed(mIntake, 0.7, 2*t_third + t_fourth)
                ),

                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new AutonTransitionFC(mChassis, -0.1, 0.0, 0.0, 0.0, -1.15, 0.5, t_fifth),
                                new ChassisDriveAutonFCAndAngle(mChassis,0.0, -1.15, 225, t_sixth),
                                new AutonTransitionFC(mChassis, 0.0, -1.15, 0.0, 0.0, 0.0, 0.0, t_fifth)
                        ),
                        new RunIntakeTimed(mIntake, 0.7, 2*t_fifth + t_sixth),
                        new SetShooterSpeedTimed(mShooter, mIndexer, 2*t_fifth + t_sixth)
                ),

                new ToggleAim(mChassis),

                new ParallelCommandGroup(
                        new SequentialCommandGroup(
                                new RunIntakeTimed(mIntake, 0.75, 0.5),
                                new RunIntakeTimed(mIntake, 0.0, 1.0)
                        ),
                        new ChassisDriveAutonFC(mChassis, 0.0, 0.0, 0.0, 1.5),
                        new RunShooterVelocityTimed(mShooter, mIndexer, mChassis, 1.5)
                )

        );


    }



}
