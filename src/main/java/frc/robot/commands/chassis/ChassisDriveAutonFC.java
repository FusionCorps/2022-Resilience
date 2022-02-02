package frc.robot.commands.chassis;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Chassis;

import static frc.robot.RobotContainer.mController;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.StrictMath.PI;

public class ChassisDriveAutonFC extends CommandBase {

    // TODO: Make command that aligns wheels w/o movement overall

    Chassis mChassis;

    boolean reset;

    double mFwdSpeed;
    double mStrSpeed;
    double mRotSpeed;
    double mTime;

    public double angle = mChassis.ahrs.getAngle();

    private Timer timer = new Timer();

    public ChassisDriveAutonFC(Chassis chassis, double fwdSpeed, double strSpeed, double rotSpeed, double time) {

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

        mChassis.solveAngles(-mFwdSpeed, mStrSpeed, mRotSpeed);


    }

    @Override
    public void execute() {

        angle = -(mChassis.ahrs.getAngle() % 360);

        mChassis.runSwerve(-mFwdSpeed*cos(angle/360*(2*PI)) + mStrSpeed*sin(angle/360*(2*PI)),
                mFwdSpeed*sin(angle/360*(2*PI)) - mStrSpeed*cos(angle/360*(2*PI)),
                mRotSpeed);

    }

    @Override
    public boolean isFinished() {
        return  timer.hasPeriodPassed(mTime);
    }

    @Override
    public void end(boolean interrupted) {
        mChassis.runSwerve(0.0, 0.0, 0.0);
    }

}

