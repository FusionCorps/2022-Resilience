package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

public class ChassisWheelsAlign extends CommandBase {

    // command that sets wheel angles to match the desired positions, can be used to reduce the importance
    // of starting configuration for autonomous

    Chassis mChassis;

    double mFwdSpeed;
    double mStrSpeed;
    double mRotSpeed;
    double mTime;

    private Timer timer = new Timer();

    public ChassisWheelsAlign(Chassis chassis, double fwdSpeed, double strSpeed, double rotSpeed, double time) {

        mChassis = chassis;

        mFwdSpeed = fwdSpeed;
        mStrSpeed = strSpeed;
        mRotSpeed = rotSpeed;
        mTime = time;

        addRequirements(mChassis);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        // on passing the parameters, this solves for the angle without passing any input to drive motors
        mChassis.solveAngles(-mFwdSpeed, -mStrSpeed, mRotSpeed);


    }

    @Override
    public void execute() {

        mChassis.solveAngles(-mFwdSpeed, mStrSpeed, mRotSpeed);

    }

    @Override
    public boolean isFinished() {
        return  timer.hasElapsed(mTime);
    }

    @Override
    public void end(boolean interrupted) {

    }

}



