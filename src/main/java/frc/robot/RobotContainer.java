package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.auton.AutonBasic;
import frc.robot.commands.chassis.FieldCentricRecord;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.commands.chassis.RunFieldCentricSwerve;
import frc.robot.commands.chassis.ToggleAim;
import frc.robot.commands.climb.ClimbManage;
import frc.robot.commands.indexer.IndexerBurst;
import frc.robot.commands.indexer.IndexerManage;
import frc.robot.commands.intake.IntakeControl;
import frc.robot.commands.shooter.AddSpeed;
import frc.robot.commands.shooter.IncrementSpeed;
import frc.robot.commands.shooter.MinusSpeed;
import frc.robot.commands.shooter.RunShooter;
import frc.robot.subsystems.*;

public class RobotContainer {

    public static final Chassis mChassis = new Chassis();
    public static final Intake mIntake = new Intake();
    public static final XboxController mController = new XboxController(0);
    public static final Indexer mIndexer = new Indexer();
    public static final Shooter mShooter = new Shooter();
    public static final Climb mClimb = new Climb();




    public RobotContainer() {
        configureButtonBindings();


        mChassis.setDefaultCommand(new RunFieldCentricSwerve(mChassis));
        mIntake.setDefaultCommand(new IntakeControl(mIntake));
        mClimb.setDefaultCommand(new ClimbManage(mClimb));
//        mIndexer.setDefaultCommand(new IndexerManage(mIndexer));

    }

    JoystickButton mBbutton = new JoystickButton(mController, XboxController.Button.kB.value);

    private void configureButtonBindings() {
        new JoystickButton(mController, XboxController.Button.kB.value)
                .whenPressed(new ResetGyro(mChassis));
        new JoystickButton(mController, XboxController.Button.kA.value)
                .whileHeld(new ZeroAxes(mChassis));
        new JoystickButton(mController, XboxController.Button.kRightBumper.value)
                .whenPressed(new ToggleAim(mChassis));
        new JoystickButton(mController, XboxController.Button.kX.value)
                .whileHeld(new RunShooter(mShooter, mIndexer, mChassis));
        new JoystickButton(mController, XboxController.Button.kY.value)
                .whenPressed(new IncrementSpeed(mShooter));
        new JoystickButton(mController, XboxController.Button.kLeftBumper.value)
                .whenPressed(new IndexerBurst(mIndexer, 0.22));
        new JoystickButton(mController, XboxController.Button.kRightBumper.value)
                .whileHeld(new ClimbManage(mClimb));
        new JoystickButton(mController, XboxController.Button.kStart.value)
                .whenPressed(new AddSpeed(mShooter));
        new JoystickButton(mController, XboxController.Button.kBack.value)
                .whenPressed(new MinusSpeed(mShooter));
    }





}
