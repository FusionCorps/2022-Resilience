package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class RunShooter extends CommandBase {

    Shooter mShooter;

    public RunShooter(Shooter shooter) {
        mShooter = shooter;
        addRequirements(mShooter);
    }

    @Override
    public void execute() {
        // TODO: Make this limelight dependent
        mShooter.setShooter(0.85);
    }

    @Override
    public void end(boolean isFinished) {
        mShooter.setShooter(0.0);
    }

}
