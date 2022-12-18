package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.commands.auton.AutonBasic;
import frc.robot.commands.chassis.*;
import frc.robot.commands.climb.*;
import frc.robot.commands.indexer.IndexerBurst;
import frc.robot.commands.indexer.IndexerManage;
import frc.robot.commands.indexer.IndexerToggle;
import frc.robot.commands.intake.IntakeControl;
import frc.robot.commands.shooter.*;
import frc.robot.subsystems.*;
import frc.robot.triggers.XboxTrigger;

public class RobotContainer {

    // subsystems and controller
    // may not be best coding practice
    public static final Chassis mChassis = new Chassis();
    public static final Intake mIntake = new Intake();
    public static final XboxController mController = new XboxController(0);
    public static final Indexer mIndexer = new Indexer();
    public static final Shooter mShooter = new Shooter();
    public static final Climb mClimb = new Climb();




    public RobotContainer() {
        configureButtonBindings();

        // default commands
        mChassis.setDefaultCommand(new RunFieldCentricSwerve(mChassis));
        mIntake.setDefaultCommand(new IntakeControl(mIntake));
        mClimb.setDefaultCommand(new ClimbManage(mClimb));
        mIndexer.setDefaultCommand(new IndexerManage(mIndexer));
        // deprecated command
//        mShooter.setDefaultCommand(new RunShooterVelocityTrigger(mShooter, mIndexer, mChassis));
        mShooter.setDefaultCommand(new ShooterSpeedManage(mShooter, mChassis));

    }



    private void configureButtonBindings() {
        // reset gyro heading on B
        new JoystickButton(mController, XboxController.Button.kB.value)
                .whenPressed(new ResetGyro(mChassis));

        // old testing command, potentially still useful
//        new JoystickButton(mController, XboxController.Button.kA.value)
//                .whileHeld(new ZeroAxes(mChassis));
        // shooting on trigger
        new XboxTrigger(mController, XboxController.Axis.kLeftTrigger.value, 0.7)
                .whileActiveOnce(new RunShooterVelocity(mShooter, mIndexer, mChassis));
        // index toggle on A
        new JoystickButton(mController, XboxController.Button.kA.value)
                .whenPressed(new IndexerToggle(mIndexer));
        // aiming on RB
        new JoystickButton(mController, XboxController.Button.kRightBumper.value)
                .whenPressed(new ToggleAim(mChassis));
        // shooting on X
        new JoystickButton(mController, XboxController.Button.kX.value)
                .whileHeld(new RunShooterVelocity(mShooter, mIndexer, mChassis));
        // toggle gyro Y
       new JoystickButton(mController, XboxController.Button.kY.value)
               .whenPressed(new ToggleGyro(mChassis));
       // toggle climb servo locks on LB
        new JoystickButton(mController, XboxController.Button.kLeftBumper.value)
                .whileHeld(new ClimbServoActuateFree(mClimb));
        // climb controls on start and back
        new JoystickButton(mController, XboxController.Button.kStart.value)
                .whileHeld(new ClimbActivate(mClimb));
        new JoystickButton(mController, XboxController.Button.kBack.value)
                .whenPressed(new ClimbPause(mClimb));
    }





}
