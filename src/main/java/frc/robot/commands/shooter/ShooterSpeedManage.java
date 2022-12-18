package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;

import static frc.robot.RobotContainer.mController;

public class ShooterSpeedManage extends CommandBase {

    // converts D-Pad input into speed adjustments.
    // also checks ShuffleBoard for updates

    Shooter mShooter;
    Chassis mChassis;

    public ShooterSpeedManage(Shooter shooter, Chassis chassis) {
        mShooter = shooter;
        mChassis = chassis;
        addRequirements(mShooter);
    }

    @Override
    public void execute() {
        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);
        if (mController.getPOV() == 90) {
            mShooter.shootK += 0.001;
        } else if (mController.getPOV() == 270) {
            mShooter.shootK -= 0.001;
        }
        mShooter.shootKTab.setDouble(mShooter.shootK);

        // rev up when aiming at target
        if (mChassis.aiming) {
            mShooter.setShooter(0.41);
        } else {
            mShooter.setShooter(0.0);
        }
    }

}
