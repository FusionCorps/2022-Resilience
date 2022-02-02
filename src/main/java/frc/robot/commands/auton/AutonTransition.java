package frc.robot.commands.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class AutonTransition extends CommandBase {

    Chassis mChassis;

    double fwd1;
    double str1;
    double rot1;

    double fwd2;
    double str2;
    double rot2;

    double fwd_rate;
    double str_rate;
    double rot_rate;

    double mTime;

    private Timer timer = new Timer();

    public AutonTransition(Chassis chassis, double forward1, double strafe1, double rotation1,
        double forward2, double strafe2, double rotation2, double time) {

        mChassis = chassis;
        addRequirements(mChassis);

        fwd1 = forward1;
        str1 = strafe1;
        rot1 = rotation1;

        fwd2 = forward2;
        str2 = strafe2;
        rot2 = rotation2;

        mTime = time;

        fwd_rate = (fwd2 - fwd1)/mTime;
        str_rate = (str2 - str1)/mTime;
        rot_rate = (rot2 - rot1)/mTime;

    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

    }

    @Override
    public void execute() {
        mChassis.runSwerve(-(fwd1 + fwd_rate*timer.get()), str1 + str_rate*timer.get(), rot1 + rot_rate*timer.get());
    }

    @Override
    public boolean isFinished() {
        return  timer.hasPeriodPassed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mChassis.runSwerve(fwd2, str2, rot2);
    }


}
