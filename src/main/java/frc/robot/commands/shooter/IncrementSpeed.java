package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.Shooter;

public class IncrementSpeed extends InstantCommand {

    // DEPRECATED DEPRECATED DEPRECATED
    // ^^^

    Shooter mShooter;

    public IncrementSpeed(Shooter shooter) {
        mShooter = shooter;
        addRequirements(mShooter);
    }

    @Override
    public void initialize() {

        double temp = mShooter.target + 0.05;
        mShooter.target = (temp % 1);

        System.out.println(mShooter.target);

        mShooter.min_vel = mShooter.target*20000 - 150;
        mShooter.max_vel = mShooter.target*20000 + 150;

    }

}
