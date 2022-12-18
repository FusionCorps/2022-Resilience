package frc.robot.commands.intake;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

import static frc.robot.RobotContainer.mController;

public class RunIntakeTimed extends CommandBase {

    // run intake during auton

    Intake mIntake;
    double mTime;

    double mPct;

    private Timer timer = new Timer();

    public RunIntakeTimed(Intake intake, double pct, double time) {
        mIntake = intake;
        addRequirements(mIntake);
        mTime = time;
        mPct = pct;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        mIntake.runIntake(mPct);
    }

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mIntake.runIntake(0.0);
    }


}
