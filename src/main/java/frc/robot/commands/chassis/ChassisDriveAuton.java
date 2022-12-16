package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class ChassisDriveAuton extends CommandBase {

    // command to put inputs to swerve base over a period of time

    // variables to initialize
    Chassis mChassis;

    boolean reset;

    double mFwdSpeed;
    double mStrSpeed;
    double mRotSpeed;
    double mTime;

    private Timer timer = new Timer();

    public ChassisDriveAuton(Chassis chassis, double fwdSpeed, double strSpeed, double rotSpeed, double time) {

        mChassis = chassis;

        mFwdSpeed = fwdSpeed;
        mStrSpeed = strSpeed;
        mRotSpeed = rotSpeed;
        mTime = time;

        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        // start timer
        timer.reset();
        timer.start();

        // align wheels to reduce the effect of slop
        mChassis.solveAngles(-mFwdSpeed, -mStrSpeed, mRotSpeed);


    }

    @Override
    public void execute() {
        // pass over period of time
        mChassis.runSwerve(-mFwdSpeed, mStrSpeed, mRotSpeed);

    }

    @Override
    public boolean isFinished() {
        // check timer
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        // stop running
        mChassis.runSwerve(0.0, 0.0, 0.0);

    }

}
