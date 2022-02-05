package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.auton.AutonBasic;
import frc.robot.commands.chassis.FieldCentricRecord;
import frc.robot.commands.chassis.ResetGyro;
import frc.robot.commands.chassis.RunFieldCentricSwerve;
import frc.robot.commands.chassis.ToggleAim;
import frc.robot.commands.indexer.IndexerManage;
import frc.robot.commands.intake.IntakeControl;
import frc.robot.commands.shooter.IncrementSpeed;
import frc.robot.commands.shooter.RunShooter;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class RobotContainer {

    public static final Chassis mChassis = new Chassis();
    public static final Intake mIntake = new Intake();
    public static final XboxController mController = new XboxController(0);
    private static final Indexer mIndexer = new Indexer();
    private static final Shooter mShooter = new Shooter();




    public RobotContainer() {
        configureButtonBindings();


        mChassis.setDefaultCommand(new RunFieldCentricSwerve(mChassis));
        mIntake.setDefaultCommand(new IntakeControl(mIntake));

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
                .whileHeld(new RunShooter(mShooter, mIndexer));
        new JoystickButton(mController, XboxController.Button.kY.value)
                .whenPressed(new IncrementSpeed(mShooter));
    }





}
