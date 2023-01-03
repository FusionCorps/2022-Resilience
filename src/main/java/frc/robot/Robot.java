// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auton.*;


import static frc.robot.RobotContainer.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private RobotContainer m_robotContainer;

  private Command m_autonomousCommand;


  // yes we did make an auton selector this offseason
  // this was uh
  // not good
  private Command m_autonomousTwoBall = new AutonAdvanced(mChassis, mShooter, mIndexer, mIntake);
  private Command m_autonomousTwoBallWallShorter = new AutonTwoBallWallShorten(mChassis, mShooter, mIndexer, mIntake);
  private Command m_autonomousOneBall = new AutonBasic(mChassis, mShooter, mIndexer);
  private Command m_autonomousThreeBall = new AutonThreeBall(mChassis, mShooter, mIndexer, mIntake);
  private Command m_autonomousYoinkySploinky = new AutonAdvancedPlusYoink(mChassis, mShooter, mIndexer, mIntake);
  private Command m_autonomousFourBall = new AutonFiveBall(mChassis, mShooter, mIndexer, mIntake);



  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();

    // zero swerve modules on wakeup
    mChassis.comboFR.zero();
    mChassis.comboBR.zero();
    mChassis.comboFL.zero();
    mChassis.comboBL.zero();

    new Thread(() -> {
      // start driver cam
      UsbCamera camera = CameraServer.startAutomaticCapture();
      camera.setVideoMode(VideoMode.PixelFormat.kMJPEG, 160, 120, 30);
    }).start();

    // Power Slider (it doesn't work we just used ShuffleBoard instead)
    SmartDashboard.putNumber("DB/Slider 0", 1.00);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();


  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    // pick the right auton here
    PathPlannerTrajectory examplePath = PathPlanner.loadPath("test_line", new PathConstraints(8, 5));

    m_autonomousCommand = mChassis.followTrajectoryCommand(examplePath, true);

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}