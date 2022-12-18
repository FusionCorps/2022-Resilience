package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Shooter;

public class RevShooterManage extends CommandBase {

    // command to set shooter to spin up while aiming

    Shooter mShooter;
    Chassis mChassis;

    public RevShooterManage(Shooter shooter, Chassis chassis) {
        mShooter = shooter;
        mChassis = chassis;
        addRequirements(mShooter, mChassis);
    }

    @Override
    public void execute() {
        if (mChassis.aiming) {
            mShooter.setShooter(0.4);
        } else {
            mShooter.setShooter(0.0);
        }
    }

}
