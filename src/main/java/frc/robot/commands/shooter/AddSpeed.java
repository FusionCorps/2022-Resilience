package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class AddSpeed extends InstantCommand {

    Shooter mShooter;

    public AddSpeed(Shooter shooter) {
        mShooter = shooter;
        addRequirements(mShooter);
    }

    @Override
    public void initialize() {
        mShooter.shootK += 0.025;
    }

}
