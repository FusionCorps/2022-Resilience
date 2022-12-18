package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class AddSpeed extends InstantCommand {

    Shooter mShooter;

    // function to add/remove speed from flywheel

    public AddSpeed(Shooter shooter) {
        mShooter = shooter;
        addRequirements(mShooter);
    }

    @Override
    public void initialize() {
        mShooter.shootK = mShooter.shootKTab.getDouble(1.0);
        mShooter.shootK += 0.025;
        mShooter.shootKTab.setDouble(mShooter.shootK);
    }

}
